package net.homeip.mleclerc.omnilinkanclient.model;

import net.homeip.mleclerc.omnilinkanclient.model.enumeration.BasicUnitControl;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.EventType;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.PhoneLineStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SecurityMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemStatus;

public interface SystemModel extends Model {
	void addListener(SystemModelListener listener);
	
	void removeListener(SystemModelListener listener);

	long getDate();
	
	long getSunrise();
	
	long getSunset();
	
	SecurityMode getSecurityMode();
	
	SystemStatus getSystemStatus();
	
	PhoneLineStatus getPhoneLineStatus();

	int getEventLogCount();
	
	long getEventLogDate(int index);
	
	EventType getEventLogEventType(int index);
	
	int getEventLogP1(int index);
	
	int getEventLogP2(int index);
	
	void loadEventLog(boolean download) throws ModelException;
	
	boolean setSecurityMode(SecurityMode securityMode) throws ModelException;
	
	boolean setAllUnitsControl(BasicUnitControl control) throws ModelException;
}
