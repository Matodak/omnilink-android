package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum ArmingStatus {
	DISARMED(0), ARMED(1), BYPASSED_BY_USER(2), BYPASSED_BY_SYSTEM(3);

	private int code;

	ArmingStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ArmingStatus fromString(String str) {
		if (str.equals("DISARMED"))
			return DISARMED;
		if (str.equals("ARMED"))
			return ARMED;
		if (str.equals("BYPASSED_BY_USER"))
			return BYPASSED_BY_USER;
		if (str.equals("BYPASSED_BY_SYSTEM"))
			return BYPASSED_BY_SYSTEM;
		return null;
	}
}