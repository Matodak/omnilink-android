package net.homeip.mleclerc.omnilinkanclient.model;

import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemType;

public interface InformationModel extends Model {
	String getVersion();

	SystemType getSystemType();

	String getLocalPhoneNumber();

	String[] getDialPhoneNumbers();
}
