package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum FanMode {
	AUTO(0), ON(1);

	private int code;

	FanMode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static FanMode fromString(String str) {
		if (str.equals("AUTO"))
			return AUTO;
		if (str.equals("ON"))
			return ON;
		return null;
	}
}