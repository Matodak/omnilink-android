package net.homeip.mleclerc.omnilinkanclient.model.omnilink;

import java.util.ArrayList;
import java.util.List;

import net.homeip.mleclerc.omnilink.CommunicationException;
import net.homeip.mleclerc.omnilink.message.ReadSetupReport;
import net.homeip.mleclerc.omnilink.message.ReadSetupRequest;
import net.homeip.mleclerc.omnilink.message.SystemInformationReport;
import net.homeip.mleclerc.omnilink.message.SystemInformationRequest;
import net.homeip.mleclerc.omnilink.message.UploadSetupMessageReport;
import net.homeip.mleclerc.omnilinkanclient.model.InformationModel;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemType;

public class InformationModelImpl extends BaseModelImpl implements InformationModel {
	private final static int DIAL_PHONE_NUMBER_COUNT = 8;
	private final static int BYTE_COUNT = 200;
	
	private SystemInformationReport cachedSystemInformationReport;
	private String[] cachedDialPhoneNumbers;
	
	public InformationModelImpl(OmniLinkModelFactory factory) {
		super(factory);
	}
	
	@Override
	public String getVersion() {
		return cachedSystemInformationReport.getVersion();
	}

	@Override
	public SystemType getSystemType() {
		return convert(cachedSystemInformationReport.getModel(), SystemType.class);
	}

	@Override
	public String getLocalPhoneNumber() {
		return cachedSystemInformationReport.getLocalPhoneNumber();
	}

	@Override
	public String[] getDialPhoneNumbers() {
		return cachedDialPhoneNumbers;
	}

	@Override
	public boolean isLoaded() {
		return cachedSystemInformationReport != null && cachedDialPhoneNumbers != null;
	}

	@Override
	public void reset() {
		cachedSystemInformationReport = null;
		cachedDialPhoneNumbers = null;
	}

	@Override
	public void load() throws ModelException {
		try {
			cachedSystemInformationReport = getSystemInformationReport();
			cachedDialPhoneNumbers = getDialPhoneNumbersInternal();
		} catch (CommunicationException ex) {
			throw new ModelException(ex);
		}
	}
	
	@Override
	public void destroy() {
	}

	private SystemInformationReport getSystemInformationReport() throws CommunicationException {
		return execute(new SystemInformationRequest());
	}
	
	private String[] getDialPhoneNumbersInternal() throws CommunicationException {
		// Read the first 2 blocks of the setup info
        ReadSetupRequest request1 = new ReadSetupRequest(0, BYTE_COUNT);
        ReadSetupReport response1 = execute(request1);
        short[] data1 = response1.getInfo();
        
        ReadSetupRequest request2 = new ReadSetupRequest(response1.getNextStartIndex(), BYTE_COUNT);
        ReadSetupReport response2 = execute(request2);
        short[] data2 = response2.getInfo();
        
        // Concatenate the 2 data blocks received
        short data[] = new short[data1.length + data2.length];
        System.arraycopy(data1, 0, data, 0, data1.length);
        System.arraycopy(data2, 0, data, data1.length, data2.length);

		// Extract phone numbers from the consolidated data block
		String[] dialPhoneNumbers = UploadSetupMessageReport.getDialPhoneNumbers(data, DIAL_PHONE_NUMBER_COUNT);
		
		// Only cache the valid phone numbers
		List<String> validDialPhoneNumbers = new ArrayList<String>();
		for (String dialPhoneNumber : dialPhoneNumbers) {
			if (!dialPhoneNumber.equals("-")) {
				validDialPhoneNumbers.add(dialPhoneNumber);
			}
		}
		
		return validDialPhoneNumbers.toArray(new String[0]);
	}
}
