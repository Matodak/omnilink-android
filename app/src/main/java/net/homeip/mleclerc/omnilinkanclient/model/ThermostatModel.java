package net.homeip.mleclerc.omnilinkanclient.model;

import net.homeip.mleclerc.omnilinkanclient.model.enumeration.FanMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.HoldMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemMode;

public interface ThermostatModel extends Model {
	void addListener(ThermostatModelListener listener);
	
	void removeListener(ThermostatModelListener listener);
	
	double getCurrentTemperature();

	double getMinTemperature();

	double getMaxTemperature();

	FanMode getFanMode();

	HoldMode getHoldMode();

	SystemMode getSystemMode();

	boolean setMinTemperature(double lowSetPoint) throws ModelException;

	boolean setMaxTemperature(double highSetPoint) throws ModelException;

	boolean setFanMode(FanMode fanMode) throws ModelException;

	boolean setHoldMode(HoldMode holdMode) throws ModelException;

	boolean setSystemMode(SystemMode systemMode) throws ModelException;
}
