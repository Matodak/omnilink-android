package net.homeip.mleclerc.omnilinkanclient.model;


public interface ModelFactory {
	SystemModel createSystemModel();
	
	ThermostatModel createThermostatModel();
	
	MessageModel createMessageModel();
	
	ButtonModel createButtonModel();
	
	UnitModel createUnitModel();
	
	ZoneModel createZoneModel();
	
	InformationModel createInformationModel();
	
	Class getPreferenceActivityClass();
	
	void destroy();
}
