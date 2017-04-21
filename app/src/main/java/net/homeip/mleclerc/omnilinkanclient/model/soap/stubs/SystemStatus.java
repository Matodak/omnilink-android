package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum SystemStatus {
	SYSTEM_OK(0), ALARM_ACTIVATION(1), SECURITY_ARMING(2), PHONE_LINE_DEAD(3), AC_POWER_OFF(4), BATTERY_LOW(5), DCM_TROUBLE(6);

	private int code;

	SystemStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static SystemStatus fromString(String str) {
		if (str.equals("SYSTEM_OK"))
			return SYSTEM_OK;
		if (str.equals("ALARM_ACTIVATION"))
			return ALARM_ACTIVATION;
		if (str.equals("SECURITY_ARMING"))
			return SECURITY_ARMING;
		if (str.equals("PHONE_LINE_DEAD"))
			return PHONE_LINE_DEAD;
		if (str.equals("AC_POWER_OFF"))
			return AC_POWER_OFF;
		if (str.equals("BATTERY_LOW"))
			return BATTERY_LOW;
		if (str.equals("DCM_TROUBLE"))
			return DCM_TROUBLE;
		return null;
	}
}