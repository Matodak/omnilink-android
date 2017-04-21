package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum SecurityMode {
	OFF(0), DISARM(1), DAY(2), NIGHT(3), AWAY(4), VACATION(5), DAY_INSTANT(6), NIGHT_DELAYED(7);

	private int code;

	SecurityMode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static SecurityMode fromString(String str) {
		if (str.equals("OFF"))
			return OFF;
		if (str.equals("DISARM"))
			return DISARM;
		if (str.equals("DAY"))
			return DAY;
		if (str.equals("NIGHT"))
			return NIGHT;
		if (str.equals("AWAY"))
			return AWAY;
		if (str.equals("VACATION"))
			return VACATION;
		if (str.equals("DAY_INSTANT"))
			return DAY_INSTANT;
		if (str.equals("NIGHT_DELAYED"))
			return NIGHT_DELAYED;
		return null;
	}
}