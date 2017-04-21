package net.homeip.mleclerc.omnilinkanclient.model;

import net.homeip.mleclerc.omnilinkanclient.model.enumeration.PhoneLineStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SecurityMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemStatus;

public interface SystemModelListener {
	void securityModeChanged(SecurityMode securityMode);
	void phoneLineStatusChanged(PhoneLineStatus phoneLineStatus);
	void systemStatusChanged(SystemStatus systemStatus);
}
