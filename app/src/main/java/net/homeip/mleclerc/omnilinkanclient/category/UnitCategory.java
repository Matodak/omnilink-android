package net.homeip.mleclerc.omnilinkanclient.category;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModel;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.UnitControl;
import net.homeip.mleclerc.omnilinkanclient.util.MySpinner;
import android.content.Context;
import android.os.Handler;
import java.util.concurrent.ExecutorService;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class UnitCategory extends Category implements UnitModelListener {
	private static class IntensityChoice {
		private final int intensity;

		private IntensityChoice(int intensity) {
			this.intensity = intensity;
		}

		@Override
		public String toString() {
			return intensity + "%";
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof IntensityChoice)) {
				return false;
			}
			return intensity == ((IntensityChoice) obj).intensity;
		}

		@Override
		public int hashCode() {
			return intensity;
		}
	}

	private static final IntensityChoice[] INTENSITY_CHOICES = buildIntensityChoices();

	private UnitModel unitModel;
	private View[] unitRows;
	private Button[] unitPowerButtons;
	private MySpinner[] unitIntensitySpinners;
	private UnitControl[] unitConditions;
	
	public UnitCategory(Context context, UnitModel unitModel, DisplayMetrics displayMetrics, ExecutorService executor) {
		super(context, displayMetrics, executor);
		
		this.unitModel = unitModel;
	}
	
	@Override
	public String getName() {
		return getContext().getString(R.string.UNITS);
	}
	
	@Override
	public View[] getViews() {
		int unitCount = unitModel.getUnitCount();
		unitRows = new View[unitCount];
		unitPowerButtons = new Button[unitCount];
		unitIntensitySpinners = new MySpinner[unitCount];
		unitConditions = new UnitControl[unitCount];
		List<View> allViews = new ArrayList<View>(unitCount);
		for (int i = 0; i < unitCount; i++) {
			final int unitIndex = i;
			final int unitNumber = unitModel.getUnitNumber(i);
			String unitName = unitModel.getUnitName(i);
			UnitControl unitCondition = unitModel.getUnitCondition(unitIndex);
			unitConditions[unitIndex] = unitCondition;
			String unitConditionLabel = getUnitConditionLabel(unitCondition);
			Execution powerExec = new Execution() {
				public boolean execute(Object selection) throws ModelException {
					String buttonText = (String) selection;

					// Toggle unit condition: ON->OFF, OFF->ON
					UnitControl newUnitCondition =
							(buttonText.equals(getContext().getString(R.string.UNIT_CONDITION_OFF)))
									? UnitControl.LEVEL_100 : UnitControl.OFF;
					
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

			Execution intensityExec = new Execution() {
				public boolean execute(Object selection) throws ModelException {
					IntensityChoice choice = (IntensityChoice) selection;
					UnitControl newCondition = getConditionForIntensity(choice.intensity);
					if (unitModel.setUnitControl(unitNumber, newCondition)) {
						unitStatusChanged(unitIndex, newCondition);
						return true;
					}
					return false;
				}
			};

			RelativeLayout row = createUnitRow();

			LinearLayout controlsLayout = new LinearLayout(getContext());
			controlsLayout.setId(View.generateViewId());
			controlsLayout.setOrientation(LinearLayout.HORIZONTAL);
			controlsLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
			RelativeLayout.LayoutParams controlsParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			controlsParams.addRule(RelativeLayout.ALIGN_PARENT_END);
			controlsParams.addRule(RelativeLayout.CENTER_VERTICAL);
			controlsLayout.setLayoutParams(controlsParams);

			TextView unitLabel = new TextView(getContext());
			unitLabel.setText(MessageFormat.format(getContext().getString(R.string.UNIT_NAME), new Object[] {Integer.valueOf(unitNumber), unitName}));
			unitLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.md_web_header_text));
			unitLabel.setTextSize(12f);
			unitLabel.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
			unitLabel.setSingleLine(true);
			unitLabel.setEllipsize(TextUtils.TruncateAt.END);
			RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			labelParams.addRule(RelativeLayout.ALIGN_PARENT_START);
			labelParams.addRule(RelativeLayout.CENTER_VERTICAL);
			labelParams.addRule(RelativeLayout.START_OF, controlsLayout.getId());
			labelParams.setMargins(0, 0, dp(8), 0);
			unitLabel.setLayoutParams(labelParams);

			Button powerButton = new Button(getContext());
			powerButton.setText(unitConditionLabel);
			powerButton.setTextSize(11f);
			powerButton.setAllCaps(false);
			powerButton.setBackgroundResource(R.drawable.weblink_action_button);
			powerButton.setTextColor(ContextCompat.getColor(getContext(), R.color.md_web_action_button_text));
			powerButton.setMinHeight(0);
			powerButton.setMinimumHeight(0);
			powerButton.setPadding(dp(8), dp(4), dp(8), dp(4));
			powerButton.setOnClickListener(new ButtonOnClickListenerImpl(powerExec));
			LinearLayout.LayoutParams powerParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			powerParams.setMargins(0, 0, dp(6), 0);
			powerButton.setLayoutParams(powerParams);

			ArrayAdapter<IntensityChoice> adapter = new ArrayAdapter<IntensityChoice>(
					getContext(),
					android.R.layout.simple_spinner_item,
					INTENSITY_CHOICES);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			MySpinner intensitySpinner = new MySpinner(getContext());
			intensitySpinner.setAdapter(adapter);
			int initialChoicePos = adapter.getPosition(new IntensityChoice(getUnitIntensity(unitCondition)));
			intensitySpinner.setSelection(initialChoicePos);
			intensitySpinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListenerImpl(initialChoicePos, intensityExec));
			intensitySpinner.setBackgroundResource(R.drawable.weblink_action_button);
			intensitySpinner.setMinimumWidth(dp(72));
			intensitySpinner.setPadding(dp(4), 0, dp(4), 0);
			LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			intensitySpinner.setLayoutParams(spinnerParams);

			controlsLayout.addView(powerButton);
			controlsLayout.addView(intensitySpinner);
			row.addView(unitLabel);
			row.addView(controlsLayout);

			unitRows[i] = row;
			unitPowerButtons[i] = powerButton;
			unitIntensitySpinners[i] = intensitySpinner;
			allViews.add(row);
		}
		
		return allViews.toArray(new View[0]);
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
		    	unitConditions[index] = unitCondition;
				String unitConditionLabel = getUnitConditionLabel(unitCondition);
				Button powerButton = unitPowerButtons[index];
				MySpinner intensitySpinner = unitIntensitySpinners[index];
				powerButton.setText(unitConditionLabel);
				setSpinnerIntensity(intensitySpinner, getUnitIntensity(unitCondition));
		    }
		});
	}
	
	private String getUnitConditionLabel(UnitControl unitCondition) {
		return (getUnitIntensity(unitCondition) == 0)
				? getContext().getString(R.string.UNIT_CONDITION_OFF)
				: getContext().getString(R.string.UNIT_CONDITION_ON);
	}

	private int getUnitIntensity(UnitControl unitCondition) {
		if (unitCondition == null || unitCondition == UnitControl.OFF) {
			return 0;
		}
		if (unitCondition == UnitControl.ON) {
			return 100;
		}
		String name = unitCondition.name();
		if (name.startsWith("LEVEL_")) {
			return Integer.parseInt(name.substring("LEVEL_".length()));
		}
		if (name.startsWith("DIM_")) {
			return Math.max(0, 100 - (Integer.parseInt(name.substring("DIM_".length())) * 10));
		}
		if (name.startsWith("BRIGHT_")) {
			return Math.min(100, Integer.parseInt(name.substring("BRIGHT_".length())) * 10);
		}
		return 0;
	}

	private UnitControl getConditionForIntensity(int intensity) {
		if (intensity <= 0) {
			return UnitControl.OFF;
		}
		if (intensity >= 100) {
			return UnitControl.LEVEL_100;
		}
		return UnitControl.valueOf("LEVEL_" + intensity);
	}

	private static IntensityChoice[] buildIntensityChoices() {
		IntensityChoice[] choices = new IntensityChoice[101];
		for (int i = 0; i <= 100; i++) {
			choices[i] = new IntensityChoice(i);
		}
		return choices;
	}

	private RelativeLayout createUnitRow() {
		RelativeLayout layout = new RelativeLayout(getContext());
		LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		rowLayoutParams.setMargins(0, dp(1), 0, 0);
		layout.setLayoutParams(rowLayoutParams);
		layout.setBackgroundResource(R.drawable.weblink_row_background);
		layout.setPadding(dp(6), dp(4), dp(6), dp(4));
		return layout;
	}

	private void setSpinnerIntensity(MySpinner spinner, int intensity) {
		if (spinner == null) {
			return;
		}
		for (int i = 0; i < INTENSITY_CHOICES.length; i++) {
			if (INTENSITY_CHOICES[i].intensity == intensity) {
				spinner.setSelectedItemPosition(i);
				return;
			}
		}
	}
}
