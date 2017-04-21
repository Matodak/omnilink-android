package net.homeip.mleclerc.omnilinkanclient.model.soap;

import net.homeip.mleclerc.omnilinkanclient.model.InformationModel;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemType;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.AegisWebService;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetSystemInformation;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetSystemInformationResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.SystemInformationResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.util.VectorString;
import android.os.RemoteException;

public class InformationModelImpl extends BaseModelImpl implements InformationModel {
	private AegisWebService client;
	private SystemInformationResponse cachedSystemInfo;

	public InformationModelImpl(AegisWebService client) {
		this.client = client;
	}
	
	public String getVersion() {
		return cachedSystemInfo.version;
	}

	public SystemType getSystemType() {
		return convert(cachedSystemInfo.systemType, SystemType.class);
	}
	
	public String getLocalPhoneNumber() {
		return cachedSystemInfo.localPhoneNumber;
	}
	
	public String[] getDialPhoneNumbers() {
		VectorString dialPhoneNumbers = cachedSystemInfo.dialPhoneNumbers;
		return (dialPhoneNumbers != null) ? dialPhoneNumbers.toArray(new String[0]) : new String[0];
	}

	@Override
	public boolean isLoaded() {
		return cachedSystemInfo != null;
	}

	@Override
	public void reset() {
		cachedSystemInfo = null;
	}
	
	@Override
	public void load() throws ModelException {
		try {
			getSystemInformationResponse();
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void destroy() {
	}

	private SystemInformationResponse getSystemInformationResponse() throws RemoteException {
		if (cachedSystemInfo == null) {
			GetSystemInformation request = new GetSystemInformation();
			GetSystemInformationResponse response = client.getSystemInformation(request);
			cachedSystemInfo = response._return;
		}
		
		return cachedSystemInfo;
	}
}
