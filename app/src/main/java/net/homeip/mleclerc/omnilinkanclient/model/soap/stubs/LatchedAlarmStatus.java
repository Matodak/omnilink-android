package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum LatchedAlarmStatus {
	SECURE(0), TRIPPED(1), RESET(2);

	private int code;

	LatchedAlarmStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static LatchedAlarmStatus fromString(String str) {
		if (str.equals("SECURE"))
			return SECURE;
		if (str.equals("TRIPPED"))
			return TRIPPED;
		if (str.equals("RESET"))
			return RESET;
		return null;
	}
}