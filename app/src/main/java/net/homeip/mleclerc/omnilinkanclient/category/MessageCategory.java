package net.homeip.mleclerc.omnilinkanclient.category;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.InformationModel;
import net.homeip.mleclerc.omnilinkanclient.model.MessageModel;
import net.homeip.mleclerc.omnilinkanclient.model.MessageModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;

public class MessageCategory extends Category implements MessageModelListener {
	class DialPhoneNumberInfo {
		private int phoneNumberIndex;
		private String phoneNumber;

		public DialPhoneNumberInfo(int phoneNumberIndex, String phoneNumber) {
			this.phoneNumberIndex = phoneNumberIndex;
			this.phoneNumber = phoneNumber;
		}

		public int getPhoneNumberIndex() {
			return phoneNumberIndex;
		}

		public String toString() {
			return MessageFormat.format(getContext().getString(R.string.MESSAGE_ACTION_PLAY),
					new Object[] { phoneNumber });
		}
	}

	private static String SHOW_MESSAGE;
	private static String CLEAR_MESSAGE;
	private static String LOG_MESSAGE;

	private MessageModel messageModel;
	private InformationModel infoModel;
	private View[] messageViews;

	public MessageCategory(Context context, MessageModel messageModel, InformationModel infoModel, DisplayMetrics displayMetrics) {
		super(context, displayMetrics);

		this.messageModel = messageModel;
		this.infoModel = infoModel;
		
		SHOW_MESSAGE = context.getString(R.string.MESSAGE_ACTION_SHOW);
		CLEAR_MESSAGE = context.getString(R.string.MESSAGE_ACTION_CLEAR);
		LOG_MESSAGE = context.getString(R.string.MESSAGE_ACTION_LOG);
	}

	@Override
	public String getName() {
		return getContext().getString(R.string.MESSAGES);
	}

	@Override
	public View[] getViews() {
		List<Object> messageChoiceList = new ArrayList<Object>();
		messageChoiceList.add(CLEAR_MESSAGE);
		messageChoiceList.add(SHOW_MESSAGE);
		messageChoiceList.add(LOG_MESSAGE);
		String[] dialPhoneNumbers = infoModel.getDialPhoneNumbers();
		if (dialPhoneNumbers != null) {
			for (int i = 0; i < dialPhoneNumbers.length; i++) {
				String dialPhoneNumber = dialPhoneNumbers[i];
				DialPhoneNumberInfo dialInfo = new DialPhoneNumberInfo(i + 1, dialPhoneNumber);
				messageChoiceList.add(dialInfo);
			}
		}		
		Object[] messageChoices = messageChoiceList.toArray(new Object[0]);
		
		int messageCount = messageModel.getMessageCount();
		messageViews = new View[messageCount];
		for (int i = 0; i < messageCount; i++) {
			final int messageNumber = messageModel.getMessageNumber(i);
			String messageName = messageModel.getMessageName(i);
			Object messageChoice = getMessageChoice(messageModel.isMessageDisplayed(i));
			Execution exec = new Execution() {				
				public boolean execute(Object selectedMessageChoice) throws ModelException {
					if (selectedMessageChoice == SHOW_MESSAGE) {
						return messageModel.showMessage(messageNumber);
					} else if (selectedMessageChoice == CLEAR_MESSAGE) {
						return messageModel.clearMessage(messageNumber);
					} else if (selectedMessageChoice == LOG_MESSAGE) {
						return messageModel.logMessage(messageNumber);
					} else if (selectedMessageChoice instanceof DialPhoneNumberInfo) {
						DialPhoneNumberInfo dialInfo = (DialPhoneNumberInfo) selectedMessageChoice;
						return messageModel.playMessage(messageNumber, dialInfo.getPhoneNumberIndex());
					} else {					
						return false;
					}
				}
			};
			final View messageView = createLabelAndChoiceView(MessageFormat.format(getContext().getString(R.string.MESSAGE_NAME), new Object[] {Integer.valueOf(messageNumber), messageName}), messageChoices, messageChoice, exec);
			messageViews[i] = messageView;
		}
		
		return messageViews;
	}

	@Override
	public boolean isLoaded() {
		return messageModel.isLoaded() && infoModel.isLoaded();
	}

	@Override
	public void reset() {
		messageModel.reset();
		infoModel.reset();
	}
	
	@Override
	public void load() throws ModelException {
		messageModel.addListener(this);
		
		messageModel.load();
		infoModel.load();
	}

	@Override
	public void destroy() {
		super.destroy();
		
		messageModel.removeListener(this);
		
		messageModel.destroy();
	}

	private Object getMessageChoice(boolean isMessageDisplayed) {
		return isMessageDisplayed ? SHOW_MESSAGE : CLEAR_MESSAGE;
	}
	
	@Override
	public void messageStatusChanged(int index, final boolean isDisplayed) {
		final View messageView = messageViews[index];
		
		Handler refresh = new Handler(Looper.getMainLooper());
		refresh.post(new Runnable() {
		    public void run()
		    {
		    	Object messageChoice = getMessageChoice(isDisplayed);
				setLabelAndChoiceView(messageView, messageChoice);
		    }
		});
	}
}
