package net.homeip.mleclerc.omnilinkanclient.model.omnilink;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.homeip.mleclerc.omnilink.CommunicationException;
import net.homeip.mleclerc.omnilink.NetworkCommunication.NotificationListener;
import net.homeip.mleclerc.omnilink.enumeration.NameTypeEnum;
import net.homeip.mleclerc.omnilink.enumeration.ObjectTypeEnum;
import net.homeip.mleclerc.omnilink.enumeration.UnitControlEnum;
import net.homeip.mleclerc.omnilink.message.UnitCommand;
import net.homeip.mleclerc.omnilink.message.UnitStatusReport;
import net.homeip.mleclerc.omnilink.message.UnitStatusReport.UnitStatusInfo;
import net.homeip.mleclerc.omnilink.message.UploadNameMessageReport.NameInfo;
import net.homeip.mleclerc.omnilink.messagebase.Message;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModel;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.UnitControl;

public class UnitModelImpl extends BaseModelImpl implements UnitModel, NotificationListener {
	private final static String CACHE_FILE = "cachedUnitNames";
	
	private List<NameInfo> cachedUnitsName;
	private List<UnitStatusInfo> cachedUnitsStatus;
	private Set<UnitModelListener> listeners = new LinkedHashSet<UnitModelListener>();
	
	public UnitModelImpl(OmniLinkModelFactory factory) {
		super(factory);
	}
	
	@Override
	public void addListener(UnitModelListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(UnitModelListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public int getUnitNumber(int index) {
		return cachedUnitsName.get(index).getObjectNo();
	}

	@Override
	public String getUnitName(int index) {
		return cachedUnitsName.get(index).getText();
	}

	@Override
	public UnitControl getUnitCondition(int index) {
		UnitControlEnum controlVal = cachedUnitsStatus.get(index).getCondition();
		return convert(controlVal, UnitControl.class);
	}

	@Override
	public int getUnitCount() {
		return cachedUnitsName.size();
	}

	@Override
	public boolean isLoaded() {
		return cachedUnitsName != null && cachedUnitsStatus != null;
	}

	@Override
	public void reset() {
		factory.removeNotificationListener(this);

		factory.delete(CACHE_FILE);
		
		cachedUnitsName = null;
		cachedUnitsStatus = null;
	}

	@Override
	public void load() throws ModelException {
		try {
			// Load the cached unit properties from the persistent cache if available or download from controller
			List<NameInfo> cachedUnitsName = factory.load(CACHE_FILE);
			this.cachedUnitsName = (cachedUnitsName != null) ? cachedUnitsName : getUnitsName();
			
			// Download the unit statuses from the controller
			cachedUnitsStatus = getUnitsStatus();
			
			//  Listen for changes to the unit statuses
			factory.addNotificationListener(this);
		} catch (CommunicationException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void destroy() {
		// Stop listening for changes to the unit statuses
		factory.removeNotificationListener(this);
		
		// Store the unit names
		factory.save(CACHE_FILE, cachedUnitsName);
	}

	@Override
	public boolean setUnitControl(int unitNumber, UnitControl condition)  throws ModelException{
		return execute(new UnitCommand(unitNumber, convert(condition, UnitControlEnum.class)));
	}
	
	@Override
	public void notify(Message notification) throws CommunicationException {
		if (notification instanceof UnitStatusReport) {
			UnitStatusReport unitStatusReport = (UnitStatusReport) notification;
			@SuppressWarnings("unchecked")
			Collection<UnitStatusInfo> newUnitsStatus = unitStatusReport.getInfoList();
			
			// For each unit reported, replace with new status and notify listeners
			for (UnitStatusInfo newUnitStatus : newUnitsStatus) {
				for (int i = 0; i < cachedUnitsStatus.size(); i++) {
					UnitStatusInfo oldUnitStatus = cachedUnitsStatus.get(i);
					if (newUnitStatus.getNumber() == oldUnitStatus.getNumber()) {
						cachedUnitsStatus.set(i, newUnitStatus);
						notifyUnitStatusChanged(i);
					}
				}
			}
		}
	}

	private List<NameInfo> getUnitsName() throws CommunicationException {
		return getObjectsName(NameTypeEnum.UNIT);
	}
	
	private List<UnitStatusInfo> getUnitsStatus() throws CommunicationException {
		return getObjectsStatusForNames(ObjectTypeEnum.UNIT, cachedUnitsName);
	}
	
	private void notifyUnitStatusChanged(int index) {
		synchronized(listeners) {
			for (UnitModelListener listener : listeners) {
				listener.unitStatusChanged(index, getUnitCondition(index));
			}
		}
	}
}
