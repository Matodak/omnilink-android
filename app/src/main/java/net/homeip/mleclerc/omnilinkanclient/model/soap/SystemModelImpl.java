package net.homeip.mleclerc.omnilinkanclient.model.soap;

import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModel;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.BasicUnitControl;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.EventType;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.PhoneLineStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SecurityMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemStatus;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.AegisWebService;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.EventLogResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetEventLog;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetEventLogResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetSystemStatus;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetSystemStatusResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetAllUnitsControl;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetAllUnitsControlResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetSecurity;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetSecurityResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SystemStatusResponse;
import android.os.RemoteException;

public class SystemModelImpl extends BaseModelImpl implements SystemModel {
	private AegisWebService client;
	private SystemStatusResponse cachedSystemStatus;
	private EventLogResponse[] cachedEventLog;

	public SystemModelImpl(AegisWebService client) {
		this.client = client;
	}

	@Override
	public void addListener(SystemModelListener listener) {
	}

	@Override
	public void removeListener(SystemModelListener listener) {
	}

	@Override
	public long getDate() {
		return cachedSystemStatus.date;
	}

	@Override
	public long getSunrise() {
		return cachedSystemStatus.sunrise;
	}

	@Override
	public long getSunset() {
		return cachedSystemStatus.sunset;
	}

	@Override
	public SecurityMode getSecurityMode() {
		return convert(cachedSystemStatus.securityMode, SecurityMode.class);
	}

	@Override
	public SystemStatus getSystemStatus() {
		return convert(cachedSystemStatus.systemStatus, SystemStatus.class);
	}

	@Override
	public PhoneLineStatus getPhoneLineStatus() {
		return convert(cachedSystemStatus.phoneLineStatus, PhoneLineStatus.class);
	}

	@Override
	public void loadEventLog(boolean download) throws ModelException {
		try {
			getEventLog(download);
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public int getEventLogCount() {
		return cachedEventLog.length;
	}

	@Override
	public long getEventLogDate(int index) {
		return cachedEventLog[index].date;
	}

	@Override
	public EventType getEventLogEventType(int index) {
		return convert(cachedEventLog[index].eventType, EventType.class);
	}

	@Override
	public int getEventLogP1(int index) {
		return cachedEventLog[index].p1;
	}

	@Override
	public int getEventLogP2(int index) {
		return cachedEventLog[index].p2;
	}

	@Override
	public boolean isLoaded() {
		return cachedSystemStatus != null;
	}

	@Override
	public void reset() {
		cachedSystemStatus = null;
		cachedEventLog = null;
	}

	@Override
	public void load() throws ModelException {
		try {
			getSystemStatusResponse();
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public boolean setSecurityMode(SecurityMode securityMode) throws ModelException {
		try {
			SetSecurity request = new SetSecurity();
			request.arg0 = convert(securityMode,
					net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SecurityMode.class);
			SetSecurityResponse response = client.setSecurity(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public boolean setAllUnitsControl(BasicUnitControl control) throws ModelException {
		try {
			SetAllUnitsControl request = new SetAllUnitsControl();
			request.arg0 = net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.BasicUnitControl.valueOf(control
					.toString());
			SetAllUnitsControlResponse response = client.setAllUnitsControl(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	private SystemStatusResponse getSystemStatusResponse() throws RemoteException {
		if (cachedSystemStatus == null) {
			GetSystemStatus request = new GetSystemStatus();
			GetSystemStatusResponse response = client.getSystemStatus(request);
			cachedSystemStatus = response._return;
		}

		return cachedSystemStatus;
	}

	private EventLogResponse[] getEventLog(boolean download) throws RemoteException {
		if (cachedEventLog == null || download) {
			GetEventLog request = new GetEventLog();
			request.arg0 = download;
			GetEventLogResponse response = client.getEventLog(request);
			cachedEventLog = response.toArray(new EventLogResponse[0]);
		}

		return cachedEventLog;
	}
}
