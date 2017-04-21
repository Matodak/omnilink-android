package net.homeip.mleclerc.omnilinkanclient.model;

public interface MessageModel extends Model {
	void addListener(MessageModelListener listener);
	
	void removeListener(MessageModelListener listener);
	
	int getMessageNumber(int index);

	String getMessageName(int index);

	boolean isMessageDisplayed(int index);

	int getMessageCount();

	boolean showMessage(int messageNumber) throws ModelException;

	public boolean clearMessage(int messageNumber) throws ModelException;

	boolean logMessage(int messageNumber) throws ModelException;

	boolean playMessage(int messageNumber, int phoneNumberIndex) throws ModelException;
}
