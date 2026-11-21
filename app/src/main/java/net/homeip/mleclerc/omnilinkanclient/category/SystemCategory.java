package net.homeip.mleclerc.omnilinkanclient.category;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModel;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.ThermostatModel;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.EventType;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.BasicUnitControl;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.PhoneLineStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SecurityMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemStatus;
import net.homeip.mleclerc.omnilinkanclient.util.EnumLabel;
import android.content.Context;
import android.os.Handler;
import java.util.concurrent.ExecutorService;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;

public class SystemCategory extends Category implements SystemModelListener {
	private static EnumLabel[] SYSTEM_STATUSES;
	private static EnumLabel[] PHONE_LINE_STATUSES;
	private static EnumLabel[] SECURITY_MODES;
	
	private SystemModel systemModel;
	private ThermostatModel thermostatModel;
	
	public SystemCategory(Context context, SystemModel systemModel, ThermostatModel thermostatModel,
			DisplayMetrics displayMetrics, ExecutorService executor) {
		super(context, displayMetrics, executor);
		
		this.systemModel = systemModel;
		this.thermostatModel = thermostatModel;
		
		SYSTEM_STATUSES = new EnumLabel[] {
			new EnumLabel<SystemStatus>(SystemStatus.AC_POWER_OFF, context.getString(R.string.SYSTEM_STATUS_AC_POWER_OFF)), 
			new EnumLabel<SystemStatus>(SystemStatus.ALARM_ACTIVATION, context.getString(R.string.SYSTEM_STATUS_ALARM_ACTIVATION)), 
			new EnumLabel<SystemStatus>(SystemStatus.BATTERY_LOW, context.getString(R.string.SYSTEM_STATUS_BATTERY_LOW)), 
			new EnumLabel<SystemStatus>(SystemStatus.DCM_TROUBLE, context.getString(R.string.SYSTEM_STATUS_DCM_TROUBLE)),
			new EnumLabel<SystemStatus>(SystemStatus.PHONE_LINE_DEAD, context.getString(R.string.SYSTEM_STATUS_PHONE_LINE_DEAD)),
			new EnumLabel<SystemStatus>(SystemStatus.SECURITY_ARMING, context.getString(R.string.SYSTEM_STATUS_SECURITY_ARMING)),
			new EnumLabel<SystemStatus>(SystemStatus.SYSTEM_OK, context.getString(R.string.SYSTEM_STATUS_SYSTEM_OK))
		};
		
		PHONE_LINE_STATUSES = new EnumLabel[] {
			new EnumLabel<PhoneLineStatus>(PhoneLineStatus.DEAD, context.getString(R.string.PHONE_LINE_STATUS_DEAD)), 
			new EnumLabel<PhoneLineStatus>(PhoneLineStatus.OFF_HOOK, context.getString(R.string.PHONE_LINE_STATUS_OFF_HOOK)), 
			new EnumLabel<PhoneLineStatus>(PhoneLineStatus.ON_HOOK, context.getString(R.string.PHONE_LINE_STATUS_ON_HOOK)), 
			new EnumLabel<PhoneLineStatus>(PhoneLineStatus.RING, context.getString(R.string.PHONE_LINE_STATUS_RING)),
			new EnumLabel<PhoneLineStatus>(PhoneLineStatus.UNKNOWN, context.getString(R.string.PHONE_LINE_STATUS_UNKNOWN))
		};
		
		SECURITY_MODES = new EnumLabel[] { 
			new EnumLabel<SecurityMode>(SecurityMode.OFF, context.getString(R.string.SECURITY_MODE_OFF)), 
			new EnumLabel<SecurityMode>(SecurityMode.DISARM, context.getString(R.string.SECURITY_MODE_DISARM)),
			new EnumLabel<SecurityMode>(SecurityMode.DAY, context.getString(R.string.SECURITY_MODE_DAY)), 
			new EnumLabel<SecurityMode>(SecurityMode.DAY_INSTANT, context.getString(R.string.SECURITY_MODE_DAY_INSTANT)),
			new EnumLabel<SecurityMode>(SecurityMode.NIGHT, context.getString(R.string.SECURITY_MODE_NIGHT)), 
			new EnumLabel<SecurityMode>(SecurityMode.NIGHT_DELAYED, context.getString(R.string.SECURITY_MODE_NIGHT_DELAYED)),
			new EnumLabel<SecurityMode>(SecurityMode.AWAY, context.getString(R.string.SECURITY_MODE_AWAY)),
			new EnumLabel<SecurityMode>(SecurityMode.VACATION, context.getString(R.string.SECURITY_MODE_VACATION))
		};
	}

	@Override
	public String getName() {
		return getContext().getString(R.string.SYSTEM);
	}
	
	@Override
	public View[] getViews() {
		java.util.ArrayList<View> views = new java.util.ArrayList<View>();
		views.add(createDateView(getContext().getString(R.string.SYSTEM_TIME),
				systemModel.getDate(), DATE_LONG | TIME_LONG));
		views.add(createLabelAndTextView("Main Area",
				EnumLabel.getEnumLabelString(SECURITY_MODES, systemModel.getSecurityMode())));
		views.add(createDateView(getContext().getString(R.string.SUNRISE_TIME),
				systemModel.getSunrise(), TIME_LONG));
		views.add(createDateView(getContext().getString(R.string.SUNSET_TIME),
				systemModel.getSunset(), TIME_LONG));

		if (thermostatModel.isLoaded()) {
			views.add(createLabelAndTextView(getContext().getString(R.string.TEMPERATURE_CURRENT),
					thermostatModel.getCurrentTemperature() + "\u00B0"));
		}

		for (int i = 0; i < Math.min(5, systemModel.getEventLogCount()); i++) {
			String date = android.text.format.DateFormat.format(
					"MM/dd 'at' h:mm a", systemModel.getEventLogDate(i)).toString();
			views.add(createLabelAndTextView(date, getEventDescription(i)));
		}
		return views.toArray(new View[0]);
	}

	@Override
	public boolean isLoaded() {
		return systemModel.isLoaded();
	}

	@Override
	public void reset() {
		systemModel.reset();
	}	
	
	@Override
	public void load() throws ModelException {
		systemModel.addListener(this);
		systemModel.load();
		systemModel.loadEventLog(false);
		thermostatModel.load();
	}

	@Override
	public void destroy() {
		super.destroy();
		
		systemModel.removeListener(this);
		
		systemModel.destroy();
	}

	@Override
	public void securityModeChanged(final SecurityMode securityMode) {
	}

	@Override
	public void phoneLineStatusChanged(final PhoneLineStatus phoneLineStatus) {
	}

	@Override
	public void systemStatusChanged(final SystemStatus systemStatus) {
	}

	private String getEventDescription(int index) {
		EventType type = systemModel.getEventLogEventType(index);
		int p1 = systemModel.getEventLogP1(index);
		int p2 = systemModel.getEventLogP2(index);
		if (type == EventType.DISARMED) return "Main Area Disarmed";
		if (type == EventType.DAY) return "Main Area Armed Day";
		if (type == EventType.NIGHT) return "Main Area Armed Night";
		if (type == EventType.AWAY) return "Main Area Armed Away";
		if (type == EventType.VACATION) return "Main Area Armed Vacation";
		if (type == EventType.ZONE_TRIPPED) return "Zone " + p1 + " tripped";
		if (type == EventType.ZONE_RESTORED) return "Zone " + p1 + " restored";
		if (type == EventType.ZONE_TROUBLE) return "Zone " + p1 + " trouble";
		if (type == EventType.ALARM_ACTIVATED) return "Alarm " + p1 + " activated";
		if (type == EventType.ALARM_RESET) return "Alarm " + p1 + " reset";
		return type.toString();
	}
}
