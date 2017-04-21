package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum HoldMode {
	OFF(0), HOLD(1);

	private int code;

	HoldMode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static HoldMode fromString(String str) {
		if (str.equals("OFF"))
			return OFF;
		if (str.equals("HOLD"))
			return HOLD;
		return null;
	}
}