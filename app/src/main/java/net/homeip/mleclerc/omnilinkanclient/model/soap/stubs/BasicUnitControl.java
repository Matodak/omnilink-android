package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum BasicUnitControl {
	OFF(0), ON(1);

	private int code;

	BasicUnitControl(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static BasicUnitControl fromString(String str) {
		if (str.equals("OFF"))
			return OFF;
		if (str.equals("ON"))
			return ON;
		return null;
	}
}