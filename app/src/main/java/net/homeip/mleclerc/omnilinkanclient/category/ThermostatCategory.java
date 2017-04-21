package net.homeip.mleclerc.omnilinkanclient.category;

import java.util.ArrayList;
import java.util.List;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.ThermostatModel;
import net.homeip.mleclerc.omnilinkanclient.model.ThermostatModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.FanMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.HoldMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemMode;
import net.homeip.mleclerc.omnilinkanclient.util.EnumLabel;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;

public class ThermostatCategory extends Category implements ThermostatModelListener {
	private static Double[] TEMPERATURES;
	private static EnumLabel[] FAN_MODES;
	private static EnumLabel[] HOLD_MODES;
	private static EnumLabel[] SYSTEM_MODES;

	private ThermostatModel thermostatModel;
	private View currentTemperatureField;
	private View minTempChoiceField;
	private View maxTempChoiceField;
	private View fanModeField;
	private View holdModeField;
	private View systemModeField;
	
	public ThermostatCategory(Context context, ThermostatModel thermostatModel, DisplayMetrics displayMetrics) {
		super(context, displayMetrics);

		this.thermostatModel = thermostatModel;

		// Build temperature choices
		List<Double> temperatureList = new ArrayList<Double>();
		for (double temp = 10.0; temp <= 30.0; temp += 0.5) {
			temperatureList.add(temp);
		}
		TEMPERATURES = temperatureList.toArray(new Double[0]);

		FAN_MODES = new EnumLabel[] {
				new EnumLabel<FanMode>(FanMode.AUTO, getContext().getString(R.string.TEMPERATURE_FAN_MODE_AUTO)),
				new EnumLabel<FanMode>(FanMode.ON, getContext().getString(R.string.TEMPERATURE_FAN_MODE_ON)) };
		HOLD_MODES = new EnumLabel[] {
				new EnumLabel<HoldMode>(HoldMode.OFF, getContext().getString(R.string.TEMPERATURE_HOLD_MODE_OFF)),
				new EnumLabel<HoldMode>(HoldMode.HOLD, getContext().getString(R.string.TEMPERATURE_HOLD_MODE_HOLD)) };
		SYSTEM_MODES = new EnumLabel[] {
				new EnumLabel<SystemMode>(SystemMode.OFF, getContext().getString(R.string.TEMPERATURE_SYSTEM_MODE_OFF)),
				new EnumLabel<SystemMode>(SystemMode.HEAT, getContext()
						.getString(R.string.TEMPERATURE_SYSTEM_MODE_HEAT)),
				new EnumLabel<SystemMode>(SystemMode.COOL, getContext()
						.getString(R.string.TEMPERATURE_SYSTEM_MODE_COOL)),
				new EnumLabel<SystemMode>(SystemMode.AUTO, getContext()
						.getString(R.string.TEMPERATURE_SYSTEM_MODE_AUTO)),
				new EnumLabel<SystemMode>(SystemMode.EMERGENCY_HEAT, getContext().getString(
						R.string.TEMPERATURE_SYSTEM_MODE_EMERGENCY_HEAT)) };
	}


	@Override
	public String getName() {
		return getContext().getString(R.string.TEMPERATURE);
	}

	@Override
	public View[] getViews() {
		// Current Temperature
		currentTemperatureField = createLabelAndTextView(getContext().getString(R.string.TEMPERATURE_CURRENT),
				String.valueOf(thermostatModel.getCurrentTemperature()));

		// Min temperature
		double minTemperature = thermostatModel.getMinTemperature();
		Execution exec = new Execution() {
			public boolean execute(Object selection) throws ModelException {
				Double selectedMinTemp = (Double) selection;
				return thermostatModel.setMinTemperature(selectedMinTemp.doubleValue());
			}
		};
		minTempChoiceField = createLabelAndChoiceView(getContext().getString(R.string.TEMPERATURE_MIN),
				TEMPERATURES, Double.valueOf(minTemperature), exec);

		// Max temperature
		double maxTemperature = thermostatModel.getMaxTemperature();
		exec = new Execution() {
			public boolean execute(Object selection) throws ModelException {
				Double selectedMaxTemp = (Double) selection;
				return thermostatModel.setMaxTemperature(selectedMaxTemp.doubleValue());
			}
		};
		maxTempChoiceField = createLabelAndChoiceView(getContext().getString(R.string.TEMPERATURE_MAX),
				TEMPERATURES, maxTemperature, exec);

		// Fan mode
		EnumLabel fanModeLabel = EnumLabel.getEnumLabel(FAN_MODES, thermostatModel.getFanMode());
		exec = new Execution() {
			public boolean execute(Object selection) throws ModelException {
				FanMode selectedFanMode = (FanMode) ((EnumLabel) selection).getEnumValue();
				return thermostatModel.setFanMode(selectedFanMode);
			}
		};
		fanModeField = createLabelAndChoiceView(getContext().getString(R.string.TEMPERATURE_FAN_MODE), FAN_MODES,
				fanModeLabel, exec);

		// Hold mode
		EnumLabel holdModeLabel = EnumLabel.getEnumLabel(HOLD_MODES, thermostatModel.getHoldMode());
		exec = new Execution() {
			public boolean execute(Object selection) throws ModelException {
				HoldMode selectedHoldMode = (HoldMode) ((EnumLabel) selection).getEnumValue();
				return thermostatModel.setHoldMode(selectedHoldMode);
			}
		};
		holdModeField = createLabelAndChoiceView(getContext().getString(R.string.TEMPERATURE_HOLD_MODE),
				HOLD_MODES, holdModeLabel, exec);

		// System mode
		EnumLabel systemModeLabel = EnumLabel.getEnumLabel(SYSTEM_MODES, thermostatModel.getSystemMode());
		exec = new Execution() {
			public boolean execute(Object selection) throws ModelException {
				SystemMode selectedSystemMode = (SystemMode) ((EnumLabel) selection).getEnumValue();
				return thermostatModel.setSystemMode(selectedSystemMode);
			}
		};
		systemModeField = createLabelAndChoiceView(getContext().getString(R.string.TEMPERATURE_SYSTEM_MODE),
				SYSTEM_MODES, systemModeLabel, exec);

		return new View[] { currentTemperatureField, minTempChoiceField, maxTempChoiceField, fanModeField,
				holdModeField, systemModeField };
	}

	@Override
	public boolean isLoaded() {
		return thermostatModel.isLoaded();
	}

	@Override
	public void reset() {
		thermostatModel.reset();
	}

	@Override
	public void load() throws ModelException {
		thermostatModel.addListener(this);
		
		thermostatModel.load();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		thermostatModel.removeListener(this);
		
		thermostatModel.destroy();
	}

	@Override
	public void thermostatStatusChanged(final double currentTemp, final double minTemp, final double maxTemp, final FanMode fanMode,
			final HoldMode holdMode, final SystemMode systemMode) {
		Handler refresh = new Handler(Looper.getMainLooper());
		refresh.post(new Runnable() {
		    public void run()
		    {
		    	setLabelAndTextView(currentTemperatureField, String.valueOf(thermostatModel.getCurrentTemperature()));
		    	setLabelAndChoiceView(minTempChoiceField, minTemp);
		    	setLabelAndChoiceView(maxTempChoiceField, maxTemp);
		    	
		    	EnumLabel fanModeLabel = EnumLabel.getEnumLabel(FAN_MODES, thermostatModel.getFanMode());
		    	setLabelAndChoiceView(fanModeField, fanModeLabel);
		    	
		    	EnumLabel holdModeLabel = EnumLabel.getEnumLabel(HOLD_MODES, thermostatModel.getHoldMode());
		    	setLabelAndChoiceView(holdModeField, holdModeLabel);
		    	
		    	EnumLabel systemModeLabel = EnumLabel.getEnumLabel(SYSTEM_MODES, thermostatModel.getSystemMode());
		    	setLabelAndChoiceView(systemModeField, systemModeLabel);
		    }
		});	
	}
}
