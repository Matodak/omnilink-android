package net.homeip.mleclerc.omnilinkanclient.model.soap;

import net.homeip.mleclerc.omnilinkanclient.model.MessageModel;
import net.homeip.mleclerc.omnilinkanclient.model.MessageModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.AegisWebService;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.ClearMessage;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.ClearMessageResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetMessageStatuses;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.GetMessageStatusesResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.LogMessage;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.LogMessageResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.MessageStatusResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.PlayMessage;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.PlayMessageResponse;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.ShowMessage;
import net.homeip.mleclerc.omnilinkanclient.model.soap.stubs.ShowMessageResponse;
import android.os.RemoteException;

public class MessageModelImpl extends BaseModelImpl implements MessageModel {
	private AegisWebService client;
	private MessageStatusResponse[] cachedMessageStatuses;

	public MessageModelImpl(AegisWebService client) {
		this.client = client;
	}
	
	@Override
	public void addListener(MessageModelListener listener) {
	}

	@Override
	public void removeListener(MessageModelListener listener) {
	}

	public int getMessageNumber(int index) {
		return cachedMessageStatuses[index].messageNumber;
	}
	
	public String getMessageName(int index) {
		return cachedMessageStatuses[index].messageName;
	}
	
	public boolean isMessageDisplayed(int index) {
		return cachedMessageStatuses[index].displayed;
	}

	public int getMessageCount() {
		return cachedMessageStatuses.length;
	}

	@Override
	public boolean isLoaded() {
		return cachedMessageStatuses != null;
	}

	@Override
	public void reset() {
		cachedMessageStatuses = null;
	}

	@Override
	public void load() throws ModelException {
		try {
			cachedMessageStatuses = null;
			getMessageStatuses();
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}
	
	@Override
	public void destroy() {
	}

	public boolean showMessage(int messageNumber) throws ModelException {
		try {
			ShowMessage request = new ShowMessage();
			request.arg0 = messageNumber;
			ShowMessageResponse response = client.showMessage(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}
	
	public boolean clearMessage(int messageNumber) throws ModelException {
		try {
			ClearMessage request = new ClearMessage();
			request.arg0 = messageNumber;
			ClearMessageResponse response = client.clearMessage(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	public boolean logMessage(int messageNumber) throws ModelException {
		try {
			LogMessage request = new LogMessage();
			request.arg0 = messageNumber;
			LogMessageResponse response = client.logMessage(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}

	public boolean playMessage(int messageNumber, int phoneNumberIndex) throws ModelException {
		try {
			PlayMessage request = new PlayMessage();
			request.arg0 = messageNumber;
			request.arg1 = phoneNumberIndex;
			PlayMessageResponse response = client.playMessage(request);
			return response._return;
		} catch (RemoteException ex) {
			throw new ModelException(ex);
		}
	}
	
	private MessageStatusResponse[] getMessageStatuses() throws RemoteException {
		if (cachedMessageStatuses == null) {	
			GetMessageStatuses request = new GetMessageStatuses();
			GetMessageStatusesResponse response = client.getMessageStatuses(request);
			cachedMessageStatuses = response.toArray(new MessageStatusResponse[0]);
		}
		
		return cachedMessageStatuses;
	}
}
