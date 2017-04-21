package net.homeip.mleclerc.omnilinkanclient.model;

import net.homeip.mleclerc.omnilinkanclient.model.enumeration.UnitControl;

public interface UnitModelListener {
	void unitStatusChanged(int index, UnitControl unitCondition);
}
