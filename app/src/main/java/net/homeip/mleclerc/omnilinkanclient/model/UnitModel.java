package net.homeip.mleclerc.omnilinkanclient.model;

import net.homeip.mleclerc.omnilinkanclient.model.enumeration.UnitControl;

public interface UnitModel extends Model {
	void addListener(UnitModelListener listener);
	
	void removeListener(UnitModelListener listener);
	
	int getUnitNumber(int index);

	String getUnitName(int index);

	UnitControl getUnitCondition(int index);

	int getUnitCount();

	boolean setUnitControl(int unitNumber, UnitControl condition) throws ModelException;
}
