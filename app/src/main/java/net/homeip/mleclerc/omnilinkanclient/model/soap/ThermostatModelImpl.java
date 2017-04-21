package net.homeip.mleclerc.omnilinkanclient.model.soap;

import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.ThermostatModel;
import net.homeip.mleclerc.omnilinkanclient.model.ThermostatModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.FanMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.HoldMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemMode;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.AegisWebService;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetThermostatsStatus;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetThermostatsStatusResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetThermostatFanMode;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetThermostatFanModeResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetThermostatHighSetPoint;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetThermostatHighSetPointResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetThermostatHoldMode;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetThermostatHoldModeResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetThermostatLowSetPoint;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetThermostatLowSetPointResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetThermostatSystemMode;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetThermostatSystemModeResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.ThermostatStatusResponse;
import android.os.RemoteException;

public class ThermostatModelImpl extends BaseModelImpl implements ThermostatModel {
	private AegisWebService client;
	private ThermostatStatusResponse cachedThermostatStatus;

	public ThermostatModelImpl(AegisWebService client) {
		this.client = client;
	}
	
	@Override
	public void addListener(ThermostatModelListener listener) {
	}

	@Override
	public void removeListener(ThermostatModelListener listener) {
	}

	public double getCurrentTemperature() {
		return cachedThermostatStatus.currentTemperature;
	}
	
	public double getMinTemperature() {
		return cachedThermostatStatus.lowSetPoint;
	}

	public double getMaxTemperature() {
		return cachedThermostatStatus.highSetPoint;
	}

	public FanMode getFanMode() {
		return convert(cachedThermostatStatus.fanMode, FanMode.class);
	}

	public HoldMode getHoldMode() {
		return convert(cachedThermostatStatus.holdMode, HoldMode.class);
	}

	public SystemMode getSystemMode() {
		return convert(cachedThermostatStatus.systemMode, SystemMode.class);
	}
	
	@Override
	public boolean isLoaded() {
		return cachedThermostatStatus != null;
	}

	@Override
	public void reset() {
		cachedThermostatStatus = null;
	}

	@Override
	public void load() throws ModelException {
		try {
			cachedThermostatStatus = null;
			getThermostatStatusResponse();
		} catch (Exception ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void destroy() {
	}

	public boolean setMinTemperature(double lowSetPoint) throws ModelException {
		try {
			SetThermostatLowSetPoint request = new SetThermostatLowSetPoint();
			request.arg0 = lowSetPoint;
			SetThermostatLowSetPointResponse response = client.setThermostatLowSetPoint(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	public boolean setMaxTemperature(double highSetPoint) throws ModelException {
		try {
			SetThermostatHighSetPoint request = new SetThermostatHighSetPoint();
			request.arg0 = highSetPoint;
			SetThermostatHighSetPointResponse response = client.setThermostatHighSetPoint(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	public boolean setFanMode(FanMode fanMode) throws ModelException {
		try {
			SetThermostatFanMode request = new SetThermostatFanMode();
			request.arg0 = convert(fanMode, net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.FanMode.class);
			SetThermostatFanModeResponse response = client.setThermostatFanMode(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	public boolean setHoldMode(HoldMode holdMode) throws ModelException {
		try {
			SetThermostatHoldMode request = new SetThermostatHoldMode();
			request.arg0 = convert(holdMode, net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.HoldMode.class);
			SetThermostatHoldModeResponse response = client.setThermostatHoldMode(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	public boolean setSystemMode(SystemMode systemMode) throws ModelException {
		try {
			SetThermostatSystemMode request = new SetThermostatSystemMode();
			request.arg0 = convert(systemMode, net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SystemMode.class);
			SetThermostatSystemModeResponse response = client.setThermostatSystemMode(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	private ThermostatStatusResponse getThermostatStatusResponse() throws RemoteException {
		if (cachedThermostatStatus == null) {
			GetThermostatsStatus request = new GetThermostatsStatus();
			GetThermostatsStatusResponse response = client.getThermostatsStatus(request);
			cachedThermostatStatus = response._return;
		}
		
		return cachedThermostatStatus;
	}
}
