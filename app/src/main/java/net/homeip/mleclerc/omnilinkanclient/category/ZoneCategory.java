package net.homeip.mleclerc.omnilinkanclient.category;

import java.text.MessageFormat;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.ZoneModel;
import net.homeip.mleclerc.omnilinkanclient.model.ZoneModelListener;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.LatchedAlarmStatus;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.ZoneCondition;
import net.homeip.mleclerc.omnilinkanclient.util.EnumLabel;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;

public class ZoneCategory extends Category implements ZoneModelListener {
	private static EnumLabel[] ZONES_CONDITIONS;
	private static EnumLabel[] ZONE_LATCHED_ALARM_STATUSES;

	private ZoneModel zoneModel;
	View[] zoneViews;
	
	public ZoneCategory(Context context, ZoneModel zoneModel, DisplayMetrics displayMetrics) {
		super(context, displayMetrics);
		
		this.zoneModel = zoneModel;

		ZONES_CONDITIONS = new EnumLabel[] { 
			new EnumLabel<ZoneCondition>(ZoneCondition.NOT_READY, getContext().getString(R.string.ZONE_CONDITION_NOT_READY)),
			new EnumLabel<ZoneCondition>(ZoneCondition.SECURE, getContext().getString(R.string.ZONE_CONDITION_SECURE)),
			new EnumLabel<ZoneCondition>(ZoneCondition.TROUBLE, getContext().getString(R.string.ZONE_CONDTION_TROUBLE))
		};

		ZONE_LATCHED_ALARM_STATUSES = new EnumLabel[] {
			new EnumLabel<LatchedAlarmStatus>(LatchedAlarmStatus.RESET, getContext().getString(R.string.ZONE_LATCHED_ALARM_STATUS_RESET)),
			new EnumLabel<LatchedAlarmStatus>(LatchedAlarmStatus.SECURE, getContext().getString(R.string.ZONE_LATCHED_ALARM_STATUS_SECURE)),
			new EnumLabel<LatchedAlarmStatus>(LatchedAlarmStatus.TRIPPED, getContext().getString(R.string.ZONE_LATCHED_ALARM_STATUS_TRIPPED))
		};
	}

	@Override
	public String getName() {
		return getContext().getString(R.string.ZONES);
	}
	
	@Override
	public View[] getViews() {
		int zoneCount = zoneModel.getZoneCount();
		zoneViews = new View[zoneCount];
		for (int i = 0; i < zoneCount; i++) {
			final int zoneNumber = zoneModel.getZoneNumber(i);
			String zoneName = zoneModel.getZoneName(i);
			ZoneCondition zoneCondition = zoneModel.getZoneCondition(i);
			LatchedAlarmStatus latchedAlarmStatus = zoneModel.getLatchedAlarmStatus(i);
			final EnumLabel zoneStatusLabel = getZoneStatusLabel(zoneCondition, latchedAlarmStatus);
			boolean zoneBypassed = zoneModel.isZoneBypassed(i);			
			Execution zoneExec = new Execution() {				
				public boolean execute(Object selection) throws ModelException {
					Boolean selectedChecked = (Boolean) selection;
					return (selectedChecked.booleanValue()) ? zoneModel.bypassZone(zoneNumber) : zoneModel.restoreZone(zoneNumber);
				}
			};
			final View zoneView = createCheckBoxAndTextView(MessageFormat.format(getContext().getString(R.string.ZONE_NAME), new Object[] {Integer.valueOf(zoneNumber), zoneName}), zoneBypassed, zoneExec, zoneStatusLabel.toString());
			zoneViews[i] = zoneView;
		}
		
		return zoneViews;
	}
	
	@Override
	public boolean isLoaded() {
		return zoneModel.isLoaded();
	}

	@Override
	public void reset() {
		zoneModel.reset();
	}
	
	@Override
	public void load() throws ModelException {
		this.zoneModel.addListener(this);
		
		zoneModel.load();
	}

	@Override
	public void destroy() {
		super.destroy();
		
		zoneModel.removeListener(this);
		
		zoneModel.destroy();
	}

	@Override
	public void zoneStatusChanged(final int index, final boolean zoneBypassed, final ZoneCondition zoneCondition, final LatchedAlarmStatus latchedAlarmStatus) {
		Handler refresh = new Handler(Looper.getMainLooper());
		refresh.post(new Runnable() {
		    public void run()
		    {
				View zoneView = zoneViews[index];
				EnumLabel zoneStatusLabel = getZoneStatusLabel(zoneCondition, latchedAlarmStatus);
				setCheckBoxAndTextView(zoneView, zoneBypassed, zoneStatusLabel.toString());
		    }
		});
	}
	
	private EnumLabel getZoneStatusLabel(ZoneCondition zoneCondition, LatchedAlarmStatus latchedAlarmStatus) {
		EnumLabel zoneConditionLabel = EnumLabel.getEnumLabel(ZONES_CONDITIONS, zoneCondition);
		EnumLabel latchedAlarmStatusLabel = EnumLabel.getEnumLabel(ZONE_LATCHED_ALARM_STATUSES, latchedAlarmStatus);
		final EnumLabel zoneStatusLabel = !latchedAlarmStatus.equals(LatchedAlarmStatus.SECURE)? latchedAlarmStatusLabel : zoneConditionLabel;
		return zoneStatusLabel;
	}
}