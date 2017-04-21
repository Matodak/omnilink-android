package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum SystemMode {
	OFF(0), HEAT(1), COOL(2), AUTO(3), EMERGENCY_HEAT(4);

	private int code;

	SystemMode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static SystemMode fromString(String str) {
		if (str.equals("OFF"))
			return OFF;
		if (str.equals("HEAT"))
			return HEAT;
		if (str.equals("COOL"))
			return COOL;
		if (str.equals("AUTO"))
			return AUTO;
		if (str.equals("EMERGENCY_HEAT"))
			return EMERGENCY_HEAT;
		return null;
	}
}