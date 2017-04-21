package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum EventType {
	ZONE_BYPASSED(0), ZONE_RESTORED(1), ALL_ZONES_RESTORED(2), DISARMED(3), DAY(4), NIGHT(5), AWAY(6), VACATION(7), DAY_INSTANT(
			8), NIGHT_DELAYED(9), ZONE_TRIPPED(10), ZONE_TROUBLE(11), REMOTE_PHONE_ACCESS(12), REMOTE_PHONE_LOCKOUT(13), ZONE_AUTO_BYPASSED(
			14), ZONE_TROUBLE_CLEARED(15), PC_ACCESS(16), ALARM_ACTIVATED(17), ALARM_RESET(18), SYSTEM_RESET(19), MESSAGE_LOGGED(
			20);

	private int code;

	EventType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static EventType fromString(String str) {
		if (str.equals("ZONE_BYPASSED"))
			return ZONE_BYPASSED;
		if (str.equals("ZONE_RESTORED"))
			return ZONE_RESTORED;
		if (str.equals("ALL_ZONES_RESTORED"))
			return ALL_ZONES_RESTORED;
		if (str.equals("DISARMED"))
			return DISARMED;
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
		if (str.equals("ZONE_TRIPPED"))
			return ZONE_TRIPPED;
		if (str.equals("ZONE_TROUBLE"))
			return ZONE_TROUBLE;
		if (str.equals("REMOTE_PHONE_ACCESS"))
			return REMOTE_PHONE_ACCESS;
		if (str.equals("REMOTE_PHONE_LOCKOUT"))
			return REMOTE_PHONE_LOCKOUT;
		if (str.equals("ZONE_AUTO_BYPASSED"))
			return ZONE_AUTO_BYPASSED;
		if (str.equals("ZONE_TROUBLE_CLEARED"))
			return ZONE_TROUBLE_CLEARED;
		if (str.equals("PC_ACCESS"))
			return PC_ACCESS;
		if (str.equals("ALARM_ACTIVATED"))
			return ALARM_ACTIVATED;
		if (str.equals("ALARM_RESET"))
			return ALARM_RESET;
		if (str.equals("SYSTEM_RESET"))
			return SYSTEM_RESET;
		if (str.equals("MESSAGE_LOGGED"))
			return MESSAGE_LOGGED;
		return null;
	}
}