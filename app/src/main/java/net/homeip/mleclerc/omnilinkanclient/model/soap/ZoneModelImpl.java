package net.homeip.mleclerc.omnilinkanclient.model.soap;

import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.ZoneModel;
import net.homeip.mleclerc.omnilinkanclient.model.ZoneModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.LatchedAlarmStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.ZoneCondition;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.AegisWebService;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.ArmingStatus;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.BypassZone;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.BypassZoneResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetZoneStatuses;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetZoneStatusesResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.RestoreZone;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.RestoreZoneResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.ZoneStatusResponse;
import android.os.RemoteException;

public class ZoneModelImpl extends BaseModelImpl implements ZoneModel {
	private AegisWebService client;
	private ZoneStatusResponse[] cachedZoneStatuses;

	public ZoneModelImpl(AegisWebService client) {
		this.client = client;
	}

	@Override
	public void addListener(ZoneModelListener listener) {
	}

	@Override
	public void removeListener(ZoneModelListener listener) {
	}

	public int getZoneNumber(int index) {
		return cachedZoneStatuses[index].zoneNumber;
	}

	public String getZoneName(int index) {
		return cachedZoneStatuses[index].zoneName;
	}

	public boolean isZoneBypassed(int index) {
		ArmingStatus armingStatusVal = cachedZoneStatuses[index].armingStatus;
		return armingStatusVal.equals(ArmingStatus.BYPASSED_BY_USER);
	}

	public ZoneCondition getZoneCondition(int index) {
		return convert(cachedZoneStatuses[index].condition, ZoneCondition.class);
	}

	public LatchedAlarmStatus getLatchedAlarmStatus(int index) {
		return convert(cachedZoneStatuses[index].latchedAlarmStatus, LatchedAlarmStatus.class);
	}

	public int getZoneCount() {
		return cachedZoneStatuses.length;
	}

	@Override
	public boolean isLoaded() {
		return cachedZoneStatuses != null;
	}

	@Override
	public void reset() {
		cachedZoneStatuses = null;
	}

	@Override
	public void load() throws ModelException {
		try {
			getZoneStatuses();
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void destroy() {
	}

	public boolean bypassZone(int zoneNumber) throws ModelException {
		try {
			BypassZone request = new BypassZone();
			request.arg0 = zoneNumber;
			BypassZoneResponse response = client.bypassZone(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	public boolean restoreZone(int zoneNumber) throws ModelException {
		try {
			RestoreZone request = new RestoreZone();
			request.arg0 = zoneNumber;
			RestoreZoneResponse response = client.restoreZone(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	private ZoneStatusResponse[] getZoneStatuses() throws RemoteException {
		if (cachedZoneStatuses == null) {
			GetZoneStatuses request = new GetZoneStatuses();
			GetZoneStatusesResponse response = client.getZoneStatuses(request);
			cachedZoneStatuses = response.toArray(new ZoneStatusResponse[0]);
		}

		return cachedZoneStatuses;
	}
}
