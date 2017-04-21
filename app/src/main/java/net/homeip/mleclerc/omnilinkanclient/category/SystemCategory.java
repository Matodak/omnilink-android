package net.homeip.mleclerc.omnilinkanclient.category;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModel;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.BasicUnitControl;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.PhoneLineStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SecurityMode;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemStatus;
import net.homeip.mleclerc.omnilinkanclient.util.EnumLabel;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;

public class SystemCategory extends Category implements SystemModelListener {
	private static EnumLabel[] SYSTEM_STATUSES;
	private static EnumLabel[] PHONE_LINE_STATUSES;
	private static EnumLabel[] SECURITY_MODES;
	
	private SystemModel systemModel;
	private View systemStatusView;
	private View phoneLineStatusView;
	private View securityModeView;
	
	public SystemCategory(Context context, SystemModel systemModel, DisplayMetrics displayMetrics) {
		super(context, displayMetrics);
		
		this.systemModel = systemModel;
		
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
			new EnumLabel<SecurityMode>(SecurityMode.DAY, context.getString(R.string.SECURITY_MODE_DAY)), 
			new EnumLabel<SecurityMode>(SecurityMode.NIGHT, context.getString(R.string.SECURITY_MODE_NIGHT)), 
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
		// System date & time
		View systemTimeView = createDateView(getContext().getString(R.string.SYSTEM_TIME), systemModel.getDate(), DATE_LONG | TIME_LONG);
		
		// System status
		systemStatusView = createLabelAndTextView(getContext().getString(R.string.SYSTEM_STATUS), EnumLabel.getEnumLabelString(SYSTEM_STATUSES, systemModel.getSystemStatus()));

		// Phone line status
		phoneLineStatusView = createLabelAndTextView(getContext().getString(R.string.PHONE_LINE_STATUS), EnumLabel.getEnumLabelString(PHONE_LINE_STATUSES, systemModel.getPhoneLineStatus()));

		// Security mode
		EnumLabel securityModeLabel = EnumLabel.getEnumLabel(SECURITY_MODES, systemModel.getSecurityMode());
		Execution<EnumLabel<SecurityMode>> securityExec = new Execution<EnumLabel<SecurityMode>>() {
			public boolean execute(EnumLabel<SecurityMode> selection) throws ModelException {
				SecurityMode selectedSecurityMode = selection.getEnumValue();
				return systemModel.setSecurityMode(selectedSecurityMode);
			}
		};
		securityModeView = createLabelAndChoiceView(getContext().getString(R.string.SECURITY_MODE), SECURITY_MODES, securityModeLabel, securityExec);
		
		// All Units On/Off
		Execution allUnitsOnExec = new Execution() {			
			public boolean execute(Object selection) throws ModelException {
				return systemModel.setAllUnitsControl(BasicUnitControl.ON);
			}
		};
		Execution allUnitsOffExec = new Execution() {			
			public boolean execute(Object selection) throws ModelException {
				return systemModel.setAllUnitsControl(BasicUnitControl.OFF);
			}
		};
		View allUnitsView = createLabelAndTwoButtonsView(getContext().getString(R.string.SYSTEM_ALL_UNITS), getContext().getString(R.string.SYSTEM_ALL_UNITS_ON), allUnitsOnExec, getContext().getString(R.string.SYSTEM_ALL_UNITS_OFF), allUnitsOffExec);
				
		return new View[] { systemTimeView, systemStatusView, phoneLineStatusView, securityModeView, allUnitsView };
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
	}

	@Override
	public void destroy() {
		super.destroy();
		
		systemModel.removeListener(this);
		
		systemModel.destroy();
	}

	@Override
	public void securityModeChanged(final SecurityMode securityMode) {
		Handler refresh = new Handler(Looper.getMainLooper());
		refresh.post(new Runnable() {
		    public void run()
		    {
		    	EnumLabel securityModeLabel = EnumLabel.getEnumLabel(SECURITY_MODES, systemModel.getSecurityMode());
		    	setLabelAndChoiceView(securityModeView, securityModeLabel);
		    }
		});
	}

	@Override
	public void phoneLineStatusChanged(final PhoneLineStatus phoneLineStatus) {
		Handler refresh = new Handler(Looper.getMainLooper());
		refresh.post(new Runnable() {
		    public void run()
		    {
		    	setLabelAndTextView(phoneLineStatusView, EnumLabel.getEnumLabelString(PHONE_LINE_STATUSES, phoneLineStatus));
		    }
		});
	}

	@Override
	public void systemStatusChanged(final SystemStatus systemStatus) {
		Handler refresh = new Handler(Looper.getMainLooper());
		refresh.post(new Runnable() {
		    public void run()
		    {
		    	setLabelAndTextView(systemStatusView, EnumLabel.getEnumLabelString(SYSTEM_STATUSES, systemStatus));
		    }
		});
	}
}
