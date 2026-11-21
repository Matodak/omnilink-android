Android application for the HAI Omni compatible systems. 

The app UI palette and chrome are aligned to the captured Web-Link skin (`default/default/skin.css`) so the Android screens match the web client look and feel.

## Web-link HTML snapshot crawler

Use this helper script to log into the web client and save HTML for in-scope pages.

1. Set credentials in environment variables.
2. Run the script.

### PowerShell example

```powershell
$env:WEBLINK_USERNAME = "<username>"
$env:WEBLINK_PASSWORD = "<password>"
python .\scripts\web_link_snapshot.py `
  --login-url "http://www.matodak.com/web-link/login.asp" `
  --base-path "/web-link/" `
  --output-dir ".\artifacts\web-link-html"
```

Outputs:
- `artifacts\web-link-html\pages\*.html` captured pages
- `artifacts\web-link-html\manifest.csv` URL-to-file mapping
- `artifacts\web-link-html\report.json` crawl summary, skips, and failures
