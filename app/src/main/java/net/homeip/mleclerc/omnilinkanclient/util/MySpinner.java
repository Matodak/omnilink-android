package net.homeip.mleclerc.omnilinkanclient.util;

import android.content.Context;
import android.widget.Spinner;

public class MySpinner extends Spinner {
	private boolean selectionSetManually;
	
	public MySpinner(Context context) {
		super(context);
	}

	public boolean isSelectionSetManually() {
		return selectionSetManually;
	}
	
	public void setSelectedItemPosition(int position) {
		if (position != getSelectedItemPosition()) {
			selectionSetManually = true;
		}
		
		setSelection(position);
	}
	
	public void clearSelectionSetManually() {
		selectionSetManually = false;
	}
}
