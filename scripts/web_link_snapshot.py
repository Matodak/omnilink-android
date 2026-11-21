#!/usr/bin/env python3
import argparse
import csv
import hashlib
import json
import os
import re
import sys
import time
from collections import deque
from html.parser import HTMLParser
from pathlib import Path
from typing import Dict, List, Optional, Set, Tuple
from urllib.parse import parse_qsl, urlencode, urljoin, urlparse, urlunparse
from urllib.request import HTTPCookieProcessor, Request, build_opener


HTML_EXTENSIONS = {"", ".asp", ".aspx", ".htm", ".html", ".php", ".jsp"}
SKIP_SCHEMES = {"mailto", "javascript", "tel"}


class FormParser(HTMLParser):
    def __init__(self) -> None:
        super().__init__()
        self.forms: List[Dict[str, object]] = []
        self._current: Optional[Dict[str, object]] = None

    def handle_starttag(self, tag: str, attrs: List[Tuple[str, Optional[str]]]) -> None:
        attr_map = {k.lower(): (v or "") for k, v in attrs}
        if tag.lower() == "form":
            self._current = {
                "action": attr_map.get("action", ""),
                "method": attr_map.get("method", "get").lower(),
                "inputs": [],
            }
            self.forms.append(self._current)
        elif tag.lower() == "input" and self._current is not None:
            input_data = {
                "name": attr_map.get("name", ""),
                "type": attr_map.get("type", "text").lower(),
                "value": attr_map.get("value", ""),
            }
            self._current["inputs"].append(input_data)

    def handle_endtag(self, tag: str) -> None:
        if tag.lower() == "form":
            self._current = None


class LinkParser(HTMLParser):
    def __init__(self) -> None:
        super().__init__()
        self.links: Set[str] = set()

    def handle_starttag(self, tag: str, attrs: List[Tuple[str, Optional[str]]]) -> None:
        if tag.lower() != "a":
            return
        for key, value in attrs:
            if key.lower() == "href" and value:
                self.links.add(value.strip())


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Login to a web client and save HTML for every in-scope page."
    )
    parser.add_argument(
        "--login-url",
        default="http://www.matodak.com/web-link/login.asp",
        help="Login page URL",
    )
    parser.add_argument(
        "--base-path",
        default="/web-link/",
        help="Only crawl URLs that start with this path",
    )
    parser.add_argument(
        "--output-dir",
        default="artifacts/web-link-html",
        help="Directory for saved HTML and report files",
    )
    parser.add_argument(
        "--username",
        default=os.environ.get("WEBLINK_USERNAME", ""),
        help="Username (default: WEBLINK_USERNAME env var)",
    )
    parser.add_argument(
        "--password",
        default=os.environ.get("WEBLINK_PASSWORD", ""),
        help="Password (default: WEBLINK_PASSWORD env var)",
    )
    parser.add_argument(
        "--max-pages",
        type=int,
        default=1000,
        help="Maximum number of pages to capture",
    )
    parser.add_argument(
        "--request-timeout-seconds",
        type=int,
        default=30,
        help="HTTP timeout per request",
    )
    parser.add_argument(
        "--delay-milliseconds",
        type=int,
        default=0,
        help="Delay between page fetches",
    )
    return parser.parse_args()


def fetch(opener, url: str, timeout: int, data: Optional[bytes] = None):
    req = Request(url, data=data)
    req.add_header(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) web-link-snapshot/1.0",
    )
    with opener.open(req, timeout=timeout) as res:
        raw = res.read()
        final_url = res.geturl()
        try:
            body = raw.decode("utf-8")
        except UnicodeDecodeError:
            body = raw.decode("latin-1", errors="replace")
        content_type = res.headers.get("Content-Type", "")
        return final_url, body, content_type


def choose_login_form(html: str) -> Optional[Dict[str, object]]:
    parser = FormParser()
    parser.feed(html)
    if not parser.forms:
        return None
    for form in parser.forms:
        inputs = form.get("inputs", [])
        if any(i.get("type") == "password" for i in inputs):
            return form
    return parser.forms[0]


def pick_username_field(inputs: List[Dict[str, str]]) -> Optional[str]:
    preferred = ["username", "user", "userid", "email", "login"]
    all_names = [i.get("name", "") for i in inputs if i.get("name")]
    for name in all_names:
        low = name.lower()
        if any(p in low for p in preferred):
            return name
    for item in inputs:
        t = item.get("type", "text")
        name = item.get("name", "")
        if name and t in {"text", "email"}:
            return name
    return None


def pick_password_field(inputs: List[Dict[str, str]]) -> Optional[str]:
    for item in inputs:
        if item.get("type") == "password" and item.get("name"):
            return item["name"]
    for item in inputs:
        name = item.get("name", "")
        if name and "pass" in name.lower():
            return name
    return None


def normalize_url(url: str) -> str:
    parsed = urlparse(url)
    normalized_query = urlencode(sorted(parse_qsl(parsed.query, keep_blank_values=True)))
    normalized = parsed._replace(fragment="", query=normalized_query)
    return urlunparse(normalized)


def is_html_like(path: str) -> bool:
    if not path:
        return True
    if path.endswith("/"):
        return True
    last = path.rsplit("/", 1)[-1]
    if "." not in last:
        return True
    ext = "." + last.rsplit(".", 1)[-1].lower()
    return ext in HTML_EXTENSIONS


def in_scope(url: str, host: str, base_path: str) -> bool:
    parsed = urlparse(url)
    if parsed.scheme not in {"http", "https"}:
        return False
    if parsed.netloc.lower() != host.lower():
        return False
    path = parsed.path or "/"
    if not path.startswith(base_path):
        return False
    return is_html_like(path)


def slugify(value: str) -> str:
    out = re.sub(r"[^a-zA-Z0-9._-]+", "_", value).strip("_")
    return out or "root"


def target_filename(url: str, index: int) -> str:
    parsed = urlparse(url)
    path = parsed.path.strip("/") or "index"
    query_suffix = f"__q_{slugify(parsed.query)}" if parsed.query else ""
    digest = hashlib.sha1(url.encode("utf-8")).hexdigest()[:10]
    return f"{index:04d}_{slugify(path)}{query_suffix}_{digest}.html"


def save_page(output_dir: Path, url: str, index: int, html: str) -> Path:
    pages_dir = output_dir / "pages"
    pages_dir.mkdir(parents=True, exist_ok=True)
    destination = pages_dir / target_filename(url, index)
    destination.write_text(html, encoding="utf-8")
    return destination


def extract_links(base_url: str, html: str) -> Set[str]:
    parser = LinkParser()
    parser.feed(html)
    links: Set[str] = set()
    for href in parser.links:
        parsed = urlparse(href)
        if parsed.scheme.lower() in SKIP_SCHEMES:
            continue
        absolute = urljoin(base_url, href)
        links.add(normalize_url(absolute))
    return links


def ensure_base_path(path: str) -> str:
    if not path.startswith("/"):
        path = "/" + path
    if not path.endswith("/"):
        path = path + "/"
    return path


def main() -> int:
    args = parse_args()
    if not args.username or not args.password:
        print(
            "Missing credentials. Set WEBLINK_USERNAME and WEBLINK_PASSWORD or pass --username/--password.",
            file=sys.stderr,
        )
        return 2

    login_url = normalize_url(args.login_url)
    base_path = ensure_base_path(args.base_path)
    output_dir = Path(args.output_dir)
    output_dir.mkdir(parents=True, exist_ok=True)

    opener = build_opener(HTTPCookieProcessor())
    timeout = args.request_timeout_seconds

    try:
        login_page_url, login_html, _ = fetch(opener, login_url, timeout)
        form = choose_login_form(login_html)
        if form is None:
            print("No login form found on login page.", file=sys.stderr)
            return 3

        inputs = form.get("inputs", [])
        payload: Dict[str, str] = {}
        for item in inputs:
            name = item.get("name", "")
            if not name:
                continue
            input_type = item.get("type", "text")
            if input_type in {"hidden", "submit"}:
                payload[name] = item.get("value", "")

        username_field = pick_username_field(inputs)
        password_field = pick_password_field(inputs)
        if not username_field or not password_field:
            print("Could not infer username/password fields from login form.", file=sys.stderr)
            return 4

        payload[username_field] = args.username
        payload[password_field] = args.password

        method = str(form.get("method", "post")).lower()
        action = str(form.get("action", ""))
        submit_url = normalize_url(urljoin(login_page_url, action or login_page_url))

        data = urlencode(payload).encode("utf-8")
        if method == "get":
            submit_url = normalize_url(submit_url + ("&" if "?" in submit_url else "?") + urlencode(payload))
            current_url, current_html, _ = fetch(opener, submit_url, timeout)
        else:
            current_url, current_html, _ = fetch(opener, submit_url, timeout, data=data)

    except Exception as exc:
        print(f"Login failed: {exc}", file=sys.stderr)
        return 5

    login_host = urlparse(login_url).netloc
    queue: deque[str] = deque()
    visited: Set[str] = set()
    skipped: List[Dict[str, str]] = []
    failures: List[Dict[str, str]] = []
    captures: List[Dict[str, str]] = []

    first = normalize_url(current_url)
    if in_scope(first, login_host, base_path):
        queue.append(first)
    else:
        skipped.append({"url": first, "reason": "post-login landing page out of scope"})

    index = 0
    delay = max(0, args.delay_milliseconds) / 1000.0

    while queue and len(visited) < args.max_pages:
        url = queue.popleft()
        if url in visited:
            continue
        visited.add(url)
        try:
            final_url, html, content_type = fetch(opener, url, timeout)
            final_url = normalize_url(final_url)
            if "html" not in content_type.lower() and content_type:
                skipped.append({"url": url, "reason": f"non-html content-type: {content_type}"})
                continue
            index += 1
            saved = save_page(output_dir, final_url, index, html)
            captures.append({"url": final_url, "file": str(saved)})
            for link in extract_links(final_url, html):
                if link in visited:
                    continue
                if in_scope(link, login_host, base_path):
                    queue.append(link)
                else:
                    skipped.append({"url": link, "reason": "out of scope"})
            if delay:
                time.sleep(delay)
        except Exception as exc:
            failures.append({"url": url, "error": str(exc)})

    manifest_path = output_dir / "manifest.csv"
    with manifest_path.open("w", encoding="utf-8", newline="") as fh:
        writer = csv.DictWriter(fh, fieldnames=["url", "file"])
        writer.writeheader()
        writer.writerows(captures)

    report = {
        "login_url": login_url,
        "base_path": base_path,
        "visited_count": len(visited),
        "captured_count": len(captures),
        "failed_count": len(failures),
        "skipped_count": len(skipped),
        "captures": captures,
        "failures": failures,
        "skipped": skipped,
    }
    report_path = output_dir / "report.json"
    report_path.write_text(json.dumps(report, indent=2), encoding="utf-8")

    print(f"Captured {len(captures)} pages.")
    print(f"Manifest: {manifest_path}")
    print(f"Report: {report_path}")
    return 0 if captures else 6


if __name__ == "__main__":
    sys.exit(main())
