package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum ZoneCondition {
	SECURE(0), NOT_READY(1), TROUBLE(2);

	private int code;

	ZoneCondition(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ZoneCondition fromString(String str) {
		if (str.equals("SECURE"))
			return SECURE;
		if (str.equals("NOT_READY"))
			return NOT_READY;
		if (str.equals("TROUBLE"))
			return TROUBLE;
		return null;
	}
}