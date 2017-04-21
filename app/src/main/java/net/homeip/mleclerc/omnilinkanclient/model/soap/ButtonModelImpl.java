package net.homeip.mleclerc.omnilinkanclient.model.soap;

import net.homeip.mleclerc.omnilinkanclient.model.ButtonModel;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.AegisWebService;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.ButtonResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.ExecuteButton;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.ExecuteButtonResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetButtons;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetButtonsResponse;
import android.os.RemoteException;

public class ButtonModelImpl extends BaseModelImpl implements ButtonModel {
	private AegisWebService client;
	private ButtonResponse[] cachedButtons;

	public ButtonModelImpl(AegisWebService client) {
		this.client = client;
	}

	public int getButtonNumber(int index) {
		return cachedButtons[index].buttonNumber;
	}

	public String getButtonName(int index) {
		return cachedButtons[index].buttonName;
	}

	public int getButtonCount() {
		return cachedButtons.length;
	}

	@Override
	public boolean isLoaded() {
		return cachedButtons != null;
	}

	@Override
	public void load() throws ModelException {
		try {
			getButtons();
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void reset() {
		cachedButtons = null;
	}

	public void destroy() {
	}

	public boolean executeButton(int buttonNumber) throws ModelException {
		try {
			ExecuteButton request = new ExecuteButton();
			request.arg0 = buttonNumber;
			ExecuteButtonResponse response = client.executeButton(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	private ButtonResponse[] getButtons() throws RemoteException {
		if (cachedButtons == null) {
			GetButtons request = new GetButtons();
			GetButtonsResponse response = client.getButtons(request);
			cachedButtons = response.toArray(new ButtonResponse[0]);
		}

		return cachedButtons;
	}
}
