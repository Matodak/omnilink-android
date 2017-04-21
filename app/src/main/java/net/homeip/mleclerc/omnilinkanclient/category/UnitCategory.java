package net.homeip.mleclerc.omnilinkanclient.category;

import java.text.MessageFormat;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModel;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.UnitControl;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;

public class UnitCategory extends Category implements UnitModelListener {
	private UnitModel unitModel;
	private View[] unitViews;
	
	public UnitCategory(Context context, UnitModel unitModel, DisplayMetrics displayMetrics) {
		super(context, displayMetrics);
		
		this.unitModel = unitModel;
	}
	
	@Override
	public String getName() {
		return getContext().getString(R.string.UNITS);
	}
	
	@Override
	public View[] getViews() {
		int unitCount = unitModel.getUnitCount();
		unitViews = new View[unitCount];
		for (int i = 0; i < unitCount; i++) {
			final int unitIndex = i;
			final int unitNumber = unitModel.getUnitNumber(i);
			String unitName = unitModel.getUnitName(i);
			UnitControl unitCondition = unitModel.getUnitCondition(unitIndex);
			String unitConditionLabel = getUnitConditionLabel(unitCondition);
			Execution exec = new Execution() {
				public boolean execute(Object selection) throws ModelException {
					String buttonText = (String) selection;

					// Toggle unit condition: ON->OFF, OFF->ON
					UnitControl newUnitCondition = (buttonText.equals(getContext().getString(R.string.UNIT_CONDITION_OFF))) ? UnitControl.ON : UnitControl.OFF;
					
					// Set the new condition on the unit itself
					if (unitModel.setUnitControl(unitNumber, newUnitCondition)) {
						// Update unit condition in view
						unitStatusChanged(unitIndex, newUnitCondition);
						
						return true;
					} else {
						return false;
					}
				}
			};
			View unitView = createLabelAndButtonView(MessageFormat.format(getContext().getString(R.string.UNIT_NAME), new Object[] {Integer.valueOf(unitNumber), unitName}), unitConditionLabel.toString(), exec);
			unitViews[i] = unitView;
		}
		
		return unitViews;
	}
	
	@Override
	public boolean isLoaded() {
		return unitModel.isLoaded();
	}

	@Override
	public void reset() {
		unitModel.reset();
	}
	
	@Override
	public void load() throws ModelException {
		unitModel.addListener(this);
		
		unitModel.load();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		unitModel.removeListener(this);
		
		unitModel.destroy();
	}

	@Override
	public void unitStatusChanged(final int index, final UnitControl unitCondition) {
		Handler refresh = new Handler(Looper.getMainLooper());
		refresh.post(new Runnable() {
		    public void run()
		    {
				String unitConditionLabel = getUnitConditionLabel(unitCondition);
				View unitView = unitViews[index];
				setLabelAndButtonView(unitView, unitConditionLabel.toString());
		    }
		});
	}
	
	private String getUnitConditionLabel(UnitControl unitCondition) {
		return (unitCondition == UnitControl.OFF) ? getContext().getString(R.string.UNIT_CONDITION_OFF) : getContext().getString(R.string.UNIT_CONDITION_ON);
	}
}
