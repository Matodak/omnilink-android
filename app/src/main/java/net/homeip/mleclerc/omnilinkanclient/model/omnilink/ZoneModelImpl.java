package net.homeip.mleclerc.omnilinkanclient.model.omnilink;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.homeip.mleclerc.omnilink.CommunicationException;
import net.homeip.mleclerc.omnilink.NetworkCommunication.NotificationListener;
import net.homeip.mleclerc.omnilink.enumeration.ArmingStatusEnum;
import net.homeip.mleclerc.omnilink.enumeration.LatchedAlarmStatusEnum;
import net.homeip.mleclerc.omnilink.enumeration.NameTypeEnum;
import net.homeip.mleclerc.omnilink.enumeration.ObjectTypeEnum;
import net.homeip.mleclerc.omnilink.enumeration.ZoneConditionEnum;
import net.homeip.mleclerc.omnilink.message.BypassZoneCommand;
import net.homeip.mleclerc.omnilink.message.MessageConstants;
import net.homeip.mleclerc.omnilink.message.RestoreZoneCommand;
import net.homeip.mleclerc.omnilink.message.UploadNameMessageReport.NameInfo;
import net.homeip.mleclerc.omnilink.message.ZoneStatusReport;
import net.homeip.mleclerc.omnilink.message.ZoneStatusReport.ZoneStatusInfo;
import net.homeip.mleclerc.omnilink.messagebase.Message;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.ZoneModel;
import net.homeip.mleclerc.omnilinkanclient.model.ZoneModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.LatchedAlarmStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.ZoneCondition;

public class ZoneModelImpl extends BaseModelImpl implements ZoneModel, NotificationListener {
	private final static String CACHE_FILE = "cachedZoneNames";
	
	private List<NameInfo> cachedZonesName;
	private List<ZoneStatusInfo> cachedZonesStatus;
	private Set<ZoneModelListener> listeners = new LinkedHashSet<ZoneModelListener>();
	
	public ZoneModelImpl(OmniLinkModelFactory factory) {
		super(factory);
	}
	
	@Override
	public void addListener(ZoneModelListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(ZoneModelListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public int getZoneNumber(int index) {
		return cachedZonesName.get(index).getObjectNo();
	}

	@Override
	public String getZoneName(int index) {
		return cachedZonesName.get(index).getText();
	}

	@Override
	public boolean isZoneBypassed(int index) {
		ArmingStatusEnum armingStatusVal = cachedZonesStatus.get(index).getArmingStatus();
		return armingStatusVal.equals(ArmingStatusEnum.BYPASSED_BY_USER);
	}

	@Override
	public ZoneCondition getZoneCondition(int index) {
		ZoneConditionEnum conditionVal = cachedZonesStatus.get(index).getCondition();
		return convert(conditionVal, ZoneCondition.class);
	}

	@Override
	public LatchedAlarmStatus getLatchedAlarmStatus(int index) {
		LatchedAlarmStatusEnum latchedAlarmStatusVal = cachedZonesStatus.get(index).getLatchedAlarmStatus();
		return convert(latchedAlarmStatusVal, LatchedAlarmStatus.class);
	}

	@Override
	public int getZoneCount() {
		return cachedZonesName.size();
	}

	@Override
	public boolean isLoaded() {
		return cachedZonesName != null && cachedZonesStatus != null;
	}
	
	@Override
	public void reset() {	
		factory.removeNotificationListener(this);

		factory.delete(CACHE_FILE);
		
		cachedZonesName = null;
		cachedZonesStatus = null;
	}

	@Override
	public void load() throws ModelException {
		try {
			// Load the cached message names from the persistent cache if available or download from controller
			List<NameInfo> cachedZonesName = factory.load(CACHE_FILE);
			this.cachedZonesName = (cachedZonesName != null) ? cachedZonesName : getZonesName();
			
			// Download the zone statuses from the controller
			cachedZonesStatus = getZonesStatus();
			
			// Listen for changes to the zone statuses
			factory.addNotificationListener(this);
		} catch (CommunicationException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void destroy() {
		factory.removeNotificationListener(this);
		
		// Store the zone names
		factory.save(CACHE_FILE, cachedZonesName);
	}

	@Override
	public boolean bypassZone(int zoneNumber) throws ModelException {
		return execute(new BypassZoneCommand(zoneNumber, MessageConstants.DEFAULT_USER_CODE));
	}

	@Override
	public boolean restoreZone(int zoneNumber) throws ModelException {
		return execute(new RestoreZoneCommand(zoneNumber, MessageConstants.DEFAULT_USER_CODE));
	}
	
	@Override
	public void notify(Message notification) throws CommunicationException {
		if (notification instanceof ZoneStatusReport) {
			ZoneStatusReport zoneStatusReport = (ZoneStatusReport) notification;
			@SuppressWarnings("unchecked")
			Collection<ZoneStatusInfo> newZonesStatus = zoneStatusReport.getInfoList();
			
			// For each zone reported, replace with new status and notify listeners
			for (ZoneStatusInfo newZoneStatus : newZonesStatus) {
				for (int i = 0; i < cachedZonesStatus.size(); i++) {
					ZoneStatusInfo oldZoneStatus = cachedZonesStatus.get(i);
					if (newZoneStatus.getNumber() == oldZoneStatus.getNumber()) {
						cachedZonesStatus.set(i, newZoneStatus);
						notifyZoneStatusChanged(i);
					}
				}
			}
		}
	}

	private List<NameInfo> getZonesName() throws CommunicationException {
		return getObjectsName(NameTypeEnum.ZONE);
	}
	
	private List<ZoneStatusInfo> getZonesStatus() throws CommunicationException {
		return getObjectsStatusForNames(ObjectTypeEnum.ZONE, cachedZonesName);
	}
	
	private void notifyZoneStatusChanged(int index) {
		synchronized(listeners) {
			for (ZoneModelListener listener : listeners) {
				listener.zoneStatusChanged(index, isZoneBypassed(index), getZoneCondition(index), getLatchedAlarmStatus(index));
			}
		}
	}
}
