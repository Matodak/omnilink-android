package net.homeip.mleclerc.omnilinkanclient.model.soap;

import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModel;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.UnitControl;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.AegisWebService;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetUnitStatuses;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetUnitStatusesResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetUnitControl;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SetUnitControlResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.UnitStatusResponse;
import android.os.RemoteException;

public class UnitModelImpl extends BaseModelImpl implements UnitModel {
	private AegisWebService client;
	private UnitStatusResponse[] cachedUnitStatuses;

	public UnitModelImpl(AegisWebService client) {
		this.client = client;
	}

	@Override
	public void addListener(UnitModelListener listener) {
	}

	@Override
	public void removeListener(UnitModelListener listener) {
	}

	public int getUnitNumber(int index) {
		return cachedUnitStatuses[index].unitNumber;
	}

	public String getUnitName(int index) {
		return cachedUnitStatuses[index].unitName;
	}

	public UnitControl getUnitCondition(int index) {
		return convert(cachedUnitStatuses[index].condition, UnitControl.class);
	}

	public int getUnitCount() {
		return cachedUnitStatuses.length;
	}

	@Override
	public boolean isLoaded() {
		return cachedUnitStatuses != null;
	}

	@Override
	public void reset() {
		cachedUnitStatuses = null;
	}

	@Override
	public void load() throws ModelException {
		try {
			getUnitStatuses();
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void destroy() {
	}

	public boolean setUnitControl(int unitNumber, UnitControl condition) throws ModelException {
		try {
			SetUnitControl request = new SetUnitControl();
			request.arg0 = unitNumber;
			request.arg1 = convert(condition, net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.UnitControl.class);
			SetUnitControlResponse response = client.setUnitControl(request);
			return response._return;
		} catch (RemoteException e) {
			return false;
		}
	}

	private UnitStatusResponse[] getUnitStatuses() throws RemoteException {
		if (cachedUnitStatuses == null) {
			GetUnitStatuses request = new GetUnitStatuses();
			GetUnitStatusesResponse response = client.getUnitStatuses(request);
			cachedUnitStatuses = response.toArray(new UnitStatusResponse[0]);
		}

		return cachedUnitStatuses;
	}
}
