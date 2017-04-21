package net.homeip.mleclerc.omnilinkanclient.model;

import net.homeip.mleclerc.omnilinkanclient.model.enumeration.LatchedAlarmStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.ZoneCondition;

public interface ZoneModelListener {
	void zoneStatusChanged(int index, boolean zoneBypassed, ZoneCondition zoneCondition, LatchedAlarmStatus latchedAlarmStatus);
}
