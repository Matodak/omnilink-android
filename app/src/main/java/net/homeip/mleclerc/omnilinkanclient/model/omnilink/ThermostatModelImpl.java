package net.homeip.mleclerc.omnilinkanclient.model.omnilink;

import java.util.LinkedHashSet;
import java.util.Set;

import net.homeip.mleclerc.omnilink.CommunicationException;
import net.homeip.mleclerc.omnilink.NetworkCommunication.NotificationListener;
import net.homeip.mleclerc.omnilink.enumeration.FanModeEnum;
import net.homeip.mleclerc.omnilink.enumeration.HoldModeEnum;
import net.homeip.mleclerc.omnilink.enumeration.ObjectTypeEnum;
import net.homeip.mleclerc.omnilink.enumeration.SystemModeEnum;
import net.homeip.mleclerc.omnilink.message.MessageConstants;
import net.homeip.mleclerc.omnilink.message.ThermostatFanModeCommand;
import net.homeip.mleclerc.omnilink.message.ThermostatHoldModeCommand;
import net.homeip.mleclerc.omnilink.message.ThermostatMaxTemperatureCommand;
import net.homeip.mleclerc.omnilink.message.ThermostatMinTemperatureCommand;
import net.homeip.mleclerc.omnilink.message.ThermostatStatusReport;
import net.homeip.mleclerc.omnilink.message.ThermostatStatusReport.ThermostatStatusInfo;
import net.homeip.mleclerc.omnilink.message.ThermostatSystemModeCommand;
import net.homeip.mleclerc.omnilink.messagebase.Message;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.ThermostatModel;
import net.homeip.mleclerc.omnilinkanclient.model.ThermostatModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.FanMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.HoldMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemMode;

public class ThermostatModelImpl extends BaseModelImpl implements ThermostatModel, NotificationListener {
	private ThermostatStatusInfo cachedThermostatStatus;
	private Set<ThermostatModelListener> listeners = new LinkedHashSet<ThermostatModelListener>();

	public ThermostatModelImpl(OmniLinkModelFactory factory) {
		super(factory);
	}
	
	@Override
	public void addListener(ThermostatModelListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(ThermostatModelListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}
	
	@Override
	public double getCurrentTemperature() {
		return cachedThermostatStatus.getCurrentTemperature();
	}

	@Override
	public double getMinTemperature() {
		return cachedThermostatStatus.getLowSetPoint();
	}

	@Override
	public double getMaxTemperature() {
		return cachedThermostatStatus.getHighSetPoint();
	}

	@Override
	public FanMode getFanMode() {
		return convert(cachedThermostatStatus.getFanMode(), FanMode.class);
	}

	@Override
	public HoldMode getHoldMode() {
		return convert(cachedThermostatStatus.getHoldStatus(), HoldMode.class);
	}

	@Override
	public SystemMode getSystemMode() {
		return convert(cachedThermostatStatus.getSystemMode(), SystemMode.class);
	}

	@Override
	public boolean isLoaded() {
		return cachedThermostatStatus != null;
	}

	@Override
	public void reset() {
		factory.removeNotificationListener(this);

		cachedThermostatStatus = null;
	}

	@Override
	public void load() throws ModelException {
		try {
			cachedThermostatStatus = getThermostatStatus();
			
			factory.addNotificationListener(this);
		} catch (CommunicationException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void destroy() {
		factory.removeNotificationListener(this);
	}

	@Override
	public boolean setMinTemperature(double lowSetPoint) throws ModelException {
		return execute(new ThermostatMinTemperatureCommand(lowSetPoint));
	}

	@Override
	public boolean setMaxTemperature(double highSetPoint) throws ModelException {
		return execute(new ThermostatMaxTemperatureCommand(highSetPoint));
	}

	@Override
	public boolean setFanMode(FanMode fanMode) throws ModelException {
		return execute(new ThermostatFanModeCommand(convert(fanMode, FanModeEnum.class)));
	}

	@Override
	public boolean setHoldMode(HoldMode holdMode) throws ModelException {
		return execute(new ThermostatHoldModeCommand(convert(holdMode, HoldModeEnum.class)));
	}

	@Override
	public boolean setSystemMode(SystemMode systemMode) throws ModelException {
		return execute(new ThermostatSystemModeCommand(convert(systemMode, SystemModeEnum.class)));
	}
	
	@Override
	public void notify(Message notification) throws CommunicationException {
		if (notification instanceof ThermostatStatusReport) {
			ThermostatStatusReport thermostatStatusReport = (ThermostatStatusReport) notification;
			cachedThermostatStatus = (ThermostatStatusInfo) thermostatStatusReport.getInfo(MessageConstants.DEFAULT_TEMP_ZONE);
			notifyThermostatStatusChanged();
		}
	}

	private ThermostatStatusInfo getThermostatStatus() throws CommunicationException {
		return getObjectStatus(ObjectTypeEnum.THERMOSTAT, MessageConstants.DEFAULT_TEMP_ZONE);
	}
	
	private void notifyThermostatStatusChanged() {
		synchronized(listeners) {
			for (ThermostatModelListener listener : listeners) {
				listener.thermostatStatusChanged(getCurrentTemperature(), getMinTemperature(), getMaxTemperature(), getFanMode(), getHoldMode(), getSystemMode());
			}
		}
	}
}
