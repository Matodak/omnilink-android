package net.homeip.mleclerc.omnilinkanclient.model.soap;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.ButtonModel;
import net.homeip.mleclerc.omnilinkanclient.model.InformationModel;
import net.homeip.mleclerc.omnilinkanclient.model.MessageModel;
import net.homeip.mleclerc.omnilinkanclient.model.ModelFactory;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModel;
import net.homeip.mleclerc.omnilinkanclient.model.ThermostatModel;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModel;
import net.homeip.mleclerc.omnilinkanclient.model.ZoneModel;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.AegisWebService;
import android.content.Context;

public class SoapModelFactory implements ModelFactory {
	private AegisWebService client;

	public SoapModelFactory(Context context) {
		client = new AegisWebService();
		client.setUrl(context.getString(R.string.WEB_SERVICE_URL));
	}

	@Override
	public SystemModel createSystemModel() {
		return new SystemModelImpl(client);
	}

	@Override
	public ThermostatModel createThermostatModel() {
		return new ThermostatModelImpl(client);
	}

	@Override
	public MessageModel createMessageModel() {
		return new MessageModelImpl(client);
	}

	@Override
	public ButtonModel createButtonModel() {
		return new ButtonModelImpl(client);
	}

	@Override
	public UnitModel createUnitModel() {
		return new UnitModelImpl(client);
	}

	@Override
	public ZoneModel createZoneModel() {
		return new ZoneModelImpl(client);
	}

	@Override
	public InformationModel createInformationModel() {
		return new InformationModelImpl(client);
	}

	@Override
	public Class getPreferenceActivityClass() {
		return null;
	}

	@Override
	public void destroy() {
	}
}
