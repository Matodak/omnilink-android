package net.homeip.mleclerc.omnilinkanclient.model;

import net.homeip.mleclerc.omnilinkanclient.model.enumeration.LatchedAlarmStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.ZoneCondition;

public interface ZoneModel extends Model {
	void addListener(ZoneModelListener listener);
	
	void removeListener(ZoneModelListener listener);
	
	int getZoneNumber(int index);

	String getZoneName(int index);

	boolean isZoneBypassed(int index);

	ZoneCondition getZoneCondition(int index);

	LatchedAlarmStatus getLatchedAlarmStatus(int index);

	int getZoneCount();

	boolean bypassZone(int zoneNumber) throws ModelException;

	boolean restoreZone(int zoneNumber) throws ModelException;
}
