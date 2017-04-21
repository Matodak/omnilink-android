package net.homeip.mleclerc.omnilinkanclient.model.omnilink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.homeip.mleclerc.omnilink.CommunicationException;
import net.homeip.mleclerc.omnilink.EndOfDataException;
import net.homeip.mleclerc.omnilink.NetworkCommunication.NotificationListener;
import net.homeip.mleclerc.omnilink.enumeration.BasicUnitControlEnum;
import net.homeip.mleclerc.omnilink.enumeration.ObjectTypeEnum;
import net.homeip.mleclerc.omnilink.enumeration.SecurityModeEnum;
import net.homeip.mleclerc.omnilink.enumeration.SystemEventTypeEnum;
import net.homeip.mleclerc.omnilink.enumeration.TroubleTypeEnum;
import net.homeip.mleclerc.omnilink.message.AllUnitsCommand;
import net.homeip.mleclerc.omnilink.message.AreaStatusReport;
import net.homeip.mleclerc.omnilink.message.AreaStatusReport.AreaStatusInfo;
import net.homeip.mleclerc.omnilink.message.MessageConstants;
import net.homeip.mleclerc.omnilink.message.ReadEventRecordReport;
import net.homeip.mleclerc.omnilink.message.ReadEventRecordRequest;
import net.homeip.mleclerc.omnilink.message.SecurityCommand;
import net.homeip.mleclerc.omnilink.message.SystemEventsReport;
import net.homeip.mleclerc.omnilink.message.SystemEventsReport.SystemEventInfo;
import net.homeip.mleclerc.omnilink.message.SystemStatusReport;
import net.homeip.mleclerc.omnilink.message.SystemStatusReport.AlarmStatusInfo;
import net.homeip.mleclerc.omnilink.message.SystemStatusRequest;
import net.homeip.mleclerc.omnilink.message.SystemTroublesReport;
import net.homeip.mleclerc.omnilink.message.SystemTroublesRequest;
import net.homeip.mleclerc.omnilink.message.UploadEventLogMessageReport.EventLogInfo;
import net.homeip.mleclerc.omnilink.messagebase.Message;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModel;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.BasicUnitControl;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.EventType;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.PhoneLineStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SecurityMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemStatus;

public class SystemModelImpl extends BaseModelImpl implements SystemModel, NotificationListener {
	private SystemStatusReport cachedSystemStatus;
	private AreaStatusInfo cachedAreaStatus;
	private List<EventLogInfo> cachedEventLog;
	private boolean cachedBatteryLow;
	private boolean cachedDcmTrouble;
	private boolean cachedAcPowerOff;
	private PhoneLineStatus cachedPhoneLineStatus = PhoneLineStatus.UNKNOWN;
	private Set<SystemModelListener> listeners = new LinkedHashSet<SystemModelListener>();
	
	public SystemModelImpl(OmniLinkModelFactory factory) {
		super(factory);
	}
	
	@Override
	public void addListener(SystemModelListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(SystemModelListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}
	
	@Override
	public long getDate() {
		return cachedSystemStatus.getDate().getTime();
	}

	@Override
	public long getSunrise() {
		return cachedSystemStatus.getSunrise().getTime();
	}

	@Override
	public long getSunset() {
		return cachedSystemStatus.getSunset().getTime();
	}

	@Override
	public SecurityMode getSecurityMode() {
		return convert(cachedAreaStatus.getAreaMode(), SecurityMode.class);
	}

	@Override
	public SystemStatus getSystemStatus() {
		AlarmStatusInfo alarmStatusInfo = cachedSystemStatus.getAlarmStatus(MessageConstants.DEFAULT_AREA);
		if (alarmStatusInfo != null && alarmStatusInfo.getAlarms().size() > 0) {
			return SystemStatus.ALARM_ACTIVATION;
		} else if (cachedBatteryLow) {
			return SystemStatus.BATTERY_LOW;
		} else if (cachedDcmTrouble) {
			return SystemStatus.DCM_TROUBLE;
		} else if (cachedAcPowerOff) {
			return SystemStatus.AC_POWER_OFF;
		} else if (cachedPhoneLineStatus == PhoneLineStatus.DEAD) {
			return SystemStatus.PHONE_LINE_DEAD;
		} else if (cachedAreaStatus.getExitTimer() > 0) {
			return SystemStatus.SECURITY_ARMING;
		} else {
			return SystemStatus.SYSTEM_OK;
		}
	}

	@Override
	public PhoneLineStatus getPhoneLineStatus() {
		return cachedPhoneLineStatus;
	}

	@Override
	public void loadEventLog(boolean download) throws ModelException {
		if (download || cachedEventLog == null) {
			try {
				cachedEventLog = getEventLog();
			} catch (CommunicationException ex) {
				throw new ModelException(ex);
			}			
		}
	}

	@Override
	public int getEventLogCount() {
		return cachedEventLog.size();
	}

	@Override
	public long getEventLogDate(int index) {
		Date date = cachedEventLog.get(index).getDate();
		return (date != null) ? date.getTime() : 0;
	}

	@Override
	public EventType getEventLogEventType(int index) {
		return convert(cachedEventLog.get(index).getType(), EventType.class);
	}

	@Override
	public int getEventLogP1(int index) {
		return cachedEventLog.get(index).getP1();
	}

	@Override
	public int getEventLogP2(int index) {
		return cachedEventLog.get(index).getP2();
	}

	@Override
	public boolean isLoaded() {
		return cachedSystemStatus != null && cachedAreaStatus != null;
	}

	@Override
	public void load() throws ModelException {		
		try {
			cachedSystemStatus = getSystemStatusReport();
			cachedAreaStatus = getAreaStatus();
			getSystemTroubles();
			
			factory.addNotificationListener(this);
		} catch (CommunicationException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void reset() {
		factory.removeNotificationListener(this);

		cachedSystemStatus = null;
		cachedAreaStatus = null;
		cachedEventLog = null;
		cachedPhoneLineStatus = PhoneLineStatus.UNKNOWN;
		cachedBatteryLow = false;
		cachedDcmTrouble = false;
		cachedAcPowerOff = false;
	}

	@Override
	public void destroy() {
		factory.removeNotificationListener(this);
	}

	@Override
	public boolean setSecurityMode(SecurityMode securityMode) throws ModelException {
		return execute(new SecurityCommand(MessageConstants.DEFAULT_AREA,
				convert(securityMode, SecurityModeEnum.class), MessageConstants.DEFAULT_USER_CODE));
	}

	@Override
	public boolean setAllUnitsControl(BasicUnitControl control) throws ModelException {
		return execute(new AllUnitsCommand(convert(control, BasicUnitControlEnum.class)));
	}

	@Override
	public void notify(Message notification) throws CommunicationException {
		if (notification instanceof SystemEventsReport) {
			SystemEventsReport systemEventsNotification = (SystemEventsReport) notification;
			@SuppressWarnings("unchecked")
			Collection<SystemEventInfo> systemEvents = systemEventsNotification.getInfoList();
			for (SystemEventInfo systemEvent : systemEvents) {
				if (systemEvent.getType() == SystemEventTypeEnum.PHONE_LINE_OFF_HOOK) {
					// Phone off hook
					cachedPhoneLineStatus = PhoneLineStatus.OFF_HOOK;
					notifyPhoneLineStatusChanged();
					notifySystemStatusChanged();
				} else if (systemEvent.getType() == SystemEventTypeEnum.PHONE_LINE_ON_HOOK) {
					// Phone on hook
					cachedPhoneLineStatus = PhoneLineStatus.ON_HOOK;
					notifyPhoneLineStatusChanged();
					notifySystemStatusChanged();
				} else if (systemEvent.getType() == SystemEventTypeEnum.PHONE_LINE_RING) {
					// Phone ringing
					cachedPhoneLineStatus = PhoneLineStatus.RING;
					notifyPhoneLineStatusChanged();
					notifySystemStatusChanged();
				} else if (systemEvent.getType() == SystemEventTypeEnum.PHONE_LINE_DEAD) {
					// Phone line dead
					cachedPhoneLineStatus = PhoneLineStatus.DEAD;
					notifyPhoneLineStatusChanged();
					notifySystemStatusChanged();
				} else if (systemEvent.getType() == SystemEventTypeEnum.BATTERY_LOW) {
					// Battery low
					cachedBatteryLow = true;
					notifySystemStatusChanged();
				} else if (systemEvent.getType() == SystemEventTypeEnum.BATTERY_OK) {
					// Battery OK
					cachedBatteryLow = false;
					notifySystemStatusChanged();
				} else if (systemEvent.getType() == SystemEventTypeEnum.DCM_TROUBLE) {
					// DCM Trouble
					cachedDcmTrouble = true;
					notifySystemStatusChanged();
				} else if (systemEvent.getType() == SystemEventTypeEnum.DCM_OK) {
					// DCM OK
					cachedDcmTrouble = false;
					notifySystemStatusChanged();
				} else if (systemEvent.getType() == SystemEventTypeEnum.AC_POWER_OFF) {
					// AC Power Off
					cachedAcPowerOff = true;
					notifySystemStatusChanged();
				} else if (systemEvent.getType() == SystemEventTypeEnum.AC_POWER_RESTORED) {
					// AC Power Restored
					cachedAcPowerOff = false;
					notifySystemStatusChanged();
				}
			}
		} else if (notification instanceof AreaStatusReport) {
			// Area status changed
			AreaStatusReport areaStatusReport = (AreaStatusReport) notification;
			cachedAreaStatus = (AreaStatusInfo) areaStatusReport.getInfo(MessageConstants.DEFAULT_AREA);
			notifySecurityModeChanged();
			notifySystemStatusChanged();
		}
	}

	private SystemStatusReport getSystemStatusReport() throws CommunicationException {
		return execute(new SystemStatusRequest());
	}

	private AreaStatusInfo getAreaStatus() throws CommunicationException {
		return getObjectStatus(ObjectTypeEnum.AREA, MessageConstants.DEFAULT_AREA);
	}

	private void getSystemTroubles() throws CommunicationException {
		SystemTroublesReport reply = execute(new SystemTroublesRequest());
		Collection<TroubleTypeEnum> troubles = reply.getTroubles();
		for (TroubleTypeEnum trouble : troubles) {
			if (trouble == TroubleTypeEnum.BATTERY_LOW) {
				cachedBatteryLow = true;
			} else if (trouble == TroubleTypeEnum.DIGITAL_COMMUNICATOR) {
				cachedDcmTrouble = true;
			} else if (trouble == TroubleTypeEnum.AC_POWER) {
				cachedAcPowerOff = true;
			}
		}
	}
	
	private List<EventLogInfo> getEventLog() throws CommunicationException {
		List<EventLogInfo> eventLog = new ArrayList<EventLogInfo>();
		
		int eventLogEntryNo = 0;
		while (true) {
			try {
				ReadEventRecordReport reply = execute(new ReadEventRecordRequest(eventLogEntryNo, -1));
				EventLogInfo eventLogInfo = reply.getInfo();
				eventLog.add(eventLogInfo);
				eventLogEntryNo = eventLogInfo.getNumber();
			} catch (EndOfDataException ex) {
				break;
			}
		}
		
		return eventLog;
	}

	private void notifyPhoneLineStatusChanged() {
		synchronized(listeners) {
			for (SystemModelListener listener : listeners) {
				listener.phoneLineStatusChanged(getPhoneLineStatus());
			}
		}
	}

	private void notifySecurityModeChanged() {
		synchronized(listeners) {
			for (SystemModelListener listener : listeners) {
				listener.securityModeChanged(getSecurityMode());
			}
		}
	}
	
	private void notifySystemStatusChanged() {
		synchronized(listeners) {
			for (SystemModelListener listener : listeners) {
				listener.systemStatusChanged(getSystemStatus());
			}
		}
	}
}
