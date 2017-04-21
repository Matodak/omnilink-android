package net.homeip.mleclerc.omnilinkanclient.model;

public interface ButtonModel extends Model {
	int getButtonNumber(int index);

	String getButtonName(int index);

	int getButtonCount();

	boolean executeButton(int buttonNumber) throws ModelException;
}
