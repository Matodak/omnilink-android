package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum PhoneLineStatus {
	UNKNOWN(0), DEAD(1), RING(2), OFF_HOOK(3), ON_HOOK(4);

	private int code;

	PhoneLineStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static PhoneLineStatus fromString(String str) {
		if (str.equals("UNKNOWN"))
			return UNKNOWN;
		if (str.equals("DEAD"))
			return DEAD;
		if (str.equals("RING"))
			return RING;
		if (str.equals("OFF_HOOK"))
			return OFF_HOOK;
		if (str.equals("ON_HOOK"))
			return ON_HOOK;
		return null;
	}
}