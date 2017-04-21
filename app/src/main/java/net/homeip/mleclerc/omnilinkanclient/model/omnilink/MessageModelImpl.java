package net.homeip.mleclerc.omnilinkanclient.model.omnilink;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.homeip.mleclerc.omnilink.CommunicationException;
import net.homeip.mleclerc.omnilink.NetworkCommunication.NotificationListener;
import net.homeip.mleclerc.omnilink.enumeration.NameTypeEnum;
import net.homeip.mleclerc.omnilink.enumeration.ObjectTypeEnum;
import net.homeip.mleclerc.omnilink.message.ClearMessageCommand;
import net.homeip.mleclerc.omnilink.message.LogMessageCommand;
import net.homeip.mleclerc.omnilink.message.MessageStatusReport;
import net.homeip.mleclerc.omnilink.message.MessageStatusReport.MessageStatusInfo;
import net.homeip.mleclerc.omnilink.message.PhoneCommand;
import net.homeip.mleclerc.omnilink.message.ShowMessageCommand;
import net.homeip.mleclerc.omnilink.message.UploadNameMessageReport.NameInfo;
import net.homeip.mleclerc.omnilink.messagebase.Message;
import net.homeip.mleclerc.omnilinkanclient.model.MessageModel;
import net.homeip.mleclerc.omnilinkanclient.model.MessageModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;

public class MessageModelImpl extends BaseModelImpl implements MessageModel, NotificationListener {
	private final static String CACHE_FILE = "cachedMessageNames";
	
	private List<NameInfo> cachedMessagesName;
	private List<MessageStatusInfo> cachedMessagesStatus;
	private Set<MessageModelListener> listeners = new LinkedHashSet<MessageModelListener>();
	
	public MessageModelImpl(OmniLinkModelFactory factory) {
		super(factory);
	}
	
	@Override
	public void addListener(MessageModelListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(MessageModelListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public int getMessageNumber(int index) {
		return cachedMessagesName.get(index).getObjectNo();
	}

	@Override
	public String getMessageName(int index) {
		return cachedMessagesName.get(index).getText();
	}

	@Override
	public boolean isMessageDisplayed(int index) {
		return cachedMessagesStatus.get(index).isDisplayed();
	}

	@Override
	public int getMessageCount() {
		return cachedMessagesName.size();
	}

	@Override
	public boolean isLoaded() {
		return cachedMessagesName != null && cachedMessagesStatus != null;
	}

	@Override
	public void reset() {
		factory.removeNotificationListener(this);
		
		factory.delete(CACHE_FILE);
		
		cachedMessagesName = null;
		cachedMessagesStatus = null;
	}

	@Override
	public void load() throws ModelException {
		try {
			// Load the cached message names from the persistent cache if available or download from controller
			List<NameInfo> cachedMessagesName = factory.load(CACHE_FILE);
			this.cachedMessagesName = (cachedMessagesName != null) ? cachedMessagesName : getMessagesName();
			
			// Download the message statuses from the controller
			cachedMessagesStatus = getMessagesStatus();
			
			// Listen for changes to the message statuses
			factory.addNotificationListener(this);
		} catch (CommunicationException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void destroy() {
		// Stop listening for changes to message statuses
		factory.removeNotificationListener(this);
		
		// Save the cached message names
		factory.save(CACHE_FILE, cachedMessagesName);
	}

	@Override
	public boolean showMessage(int messageNumber) throws ModelException {
		return execute(new ShowMessageCommand(messageNumber));
	}

	@Override
	public boolean clearMessage(int messageNumber) throws ModelException {
		return execute(new ClearMessageCommand(messageNumber));
	}

	@Override
	public boolean logMessage(int messageNumber) throws ModelException {
		return execute(new LogMessageCommand(messageNumber));
	}

	@Override
	public boolean playMessage(int messageNumber, int phoneNumberIndex) throws ModelException {
		return execute(new PhoneCommand(phoneNumberIndex, messageNumber));
	}
	
	@Override
	public void notify(Message notification) throws CommunicationException {
		if (notification instanceof MessageStatusReport) {
			MessageStatusReport messageStatusReport = (MessageStatusReport) notification;
			@SuppressWarnings("unchecked")
			Collection<MessageStatusInfo> newMessagesStatus = messageStatusReport.getInfoList();
			
			// For each message reported, replace with new status and notify listeners
			for (MessageStatusInfo newMessageStatus : newMessagesStatus) {
				for (int i = 0; i < cachedMessagesStatus.size(); i++) {
					MessageStatusInfo oldMessageStatus = cachedMessagesStatus.get(i);
					if (newMessageStatus.getNumber() == oldMessageStatus.getNumber()) {
						cachedMessagesStatus.set(i, newMessageStatus);
						notifyMessageStatusChanged(i);
					}
				}
			}
		}
	}

	private List<NameInfo> getMessagesName() throws CommunicationException {
		return getObjectsName(NameTypeEnum.MESSAGE);
	}
	
	private List<MessageStatusInfo> getMessagesStatus() throws CommunicationException {
		return getObjectsStatusForNames(ObjectTypeEnum.MESSAGE, cachedMessagesName);
	}
	
	private void notifyMessageStatusChanged(int index) {
		synchronized(listeners) {
			for (MessageModelListener listener : listeners) {
				listener.messageStatusChanged(index, isMessageDisplayed(index));
			}
		}
	}
}
