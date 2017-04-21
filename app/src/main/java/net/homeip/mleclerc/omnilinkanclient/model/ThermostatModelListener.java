package net.homeip.mleclerc.omnilinkanclient.model;

import net.homeip.mleclerc.omnilinkanclient.model.enumeration.FanMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.HoldMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemMode;

public interface ThermostatModelListener {
	void thermostatStatusChanged(double currentTemp, double minTemp, double maxTemp, FanMode fanMode, HoldMode holdMode, SystemMode systemMode);
}
