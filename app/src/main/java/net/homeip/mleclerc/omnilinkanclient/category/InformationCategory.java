package net.homeip.mleclerc.omnilinkanclient.category;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.InformationModel;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModel;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.EventType;
import net.homeip.mleclerc.omnilinkanclient.model.enumeration.SystemType;
import net.homeip.mleclerc.omnilinkanclient.util.EnumLabel;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class InformationCategory extends Category {
	private static EnumLabel[] SYSTEM_TYPES;

	private InformationModel infoModel;
	private SystemModel systemModel;
	private Dialog eventLogDialog;

	public InformationCategory(Context context, InformationModel infoModel, SystemModel systemModel, DisplayMetrics displayMetrics) {
		super(context, displayMetrics);

		this.infoModel = infoModel;
		this.systemModel = systemModel;

		SYSTEM_TYPES = new EnumLabel[] {
				new EnumLabel<SystemType>(SystemType.AEGIS, context.getString(R.string.SYSTEM_TYPE_AEGIS)),
				new EnumLabel<SystemType>(SystemType.AEGIS_2000, context.getString(R.string.SYSTEM_TYPE_AEGIS_2000)),
				new EnumLabel<SystemType>(SystemType.HAI_OMNI, context.getString(R.string.SYSTEM_TYPE_HAI_OMNI)),
				new EnumLabel<SystemType>(SystemType.HAI_OMNI_II, context.getString(R.string.SYSTEM_TYPE_HAI_OMNI_II)),
				new EnumLabel<SystemType>(SystemType.HAI_OMNI_IIE, context.getString(R.string.SYSTEM_TYPE_HAI_OMNI_IIE)),
				new EnumLabel<SystemType>(SystemType.HAI_OMNI_LT, context.getString(R.string.SYSTEM_TYPE_HAI_OMNI_LT)),
				new EnumLabel<SystemType>(SystemType.HAI_OMNI_PRO, context.getString(R.string.SYSTEM_TYPE_HAI_OMNI_PRO)),
				new EnumLabel<SystemType>(SystemType.HAI_OMNI_PRO_II, context.getString(R.string.SYSTEM_TYPE_HAI_OMNI_PRO_II)) };
		
		eventLogDialog = new Dialog(getContext());
		eventLogDialog.setTitle(R.string.EVENTLOG);
	}

	@Override
	public void destroy() {
		super.destroy();
		
		eventLogDialog.dismiss();

		infoModel.destroy();
	}

	@Override
	public String getName() {
		return getContext().getString(R.string.INFORMATION);
	}

	@Override
	public View[] getViews() {
		// System type
		View systemTypeView = createLabelAndTextView(getContext().getString(R.string.SYSTEM_TYPE),
				EnumLabel.getEnumLabelString(SYSTEM_TYPES, infoModel.getSystemType()));

		// System version
		View systemVersionView = createLabelAndTextView(getContext().getString(R.string.SYSTEM_VERSION),
				infoModel.getVersion());

		// Local phone number
		View localPhoneNumberView = createLabelAndTextView(getContext().getString(R.string.LOCAL_PHONE_NUMBER),
				infoModel.getLocalPhoneNumber());

		// Sunrise
		View sunriseView = createDateView(getContext().getString(R.string.SUNRISE_TIME), systemModel.getSunrise(), TIME_LONG);

		// Sunset
		View sunsetView = createDateView(getContext().getString(R.string.SUNSET_TIME), systemModel.getSunset(), TIME_LONG);

		// Event Log
		class EventLogExec extends Execution {
			private boolean download;
			private int eventLogCount;
			
			public EventLogExec(boolean download) {
				this.download = download;
			}
			
			public boolean execute(Object selection) throws ModelException {
				systemModel.loadEventLog(download);
				eventLogCount = systemModel.getEventLogCount();
				return eventLogCount > 0;
			}
			
			public void postExecute(boolean success) {
				if (success) {
					displayEventLog(eventLogCount);
				}
			}
			
			public boolean isDisplayToast() {
				return eventLogCount == 0;
			}
		};
		View eventLogView = createLabelAndTwoButtonsView(getContext().getString(R.string.EVENTLOG), getContext()
				.getString(R.string.EVENTLOG_VIEW), new EventLogExec(false), getContext().getString(R.string.EVENTLOG_LOAD),
				new EventLogExec(true));

		return new View[] { systemTypeView, systemVersionView, localPhoneNumberView, sunriseView, sunsetView,
				eventLogView };
	}

	@Override
	public boolean isLoaded() {
		return infoModel.isLoaded() && systemModel.isLoaded();
	}

	@Override
	public void reset() {
		infoModel.reset();
		systemModel.reset();
	}
	
	@Override
	public void load() throws ModelException {
		infoModel.load();
		systemModel.load();
	}

	private void displayEventLog(int eventLogCount) {
		ScrollView eventLogView = new ScrollView(getContext());
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < eventLogCount; i++) {
			long eventLogEntryDate = systemModel.getEventLogDate(i);
			String eventLogEntryText = getEventLogDescription(i);
			Calendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(eventLogEntryDate);
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()); 
			TextView eventLogEntryDateView = new TextView(getContext());
			eventLogEntryDateView.setText(df.format(calendar.getTime()));
			layout.addView(eventLogEntryDateView);
			TextView eventLogTextView = new TextView(getContext());
			eventLogTextView.setText(eventLogEntryText);
			layout.addView(eventLogTextView);					
		}
		eventLogView.addView(layout);
		eventLogDialog.setContentView(eventLogView);		
		eventLogDialog.show();
	}

	private String getEventLogDescription(int eventLogEntryIndex) {
		EventType eventType = systemModel.getEventLogEventType(eventLogEntryIndex);
		int p1 = systemModel.getEventLogP1(eventLogEntryIndex);
		int p2 = systemModel.getEventLogP2(eventLogEntryIndex);
		Object[] params = new Object[] {Integer.valueOf(p1), Integer.valueOf(p2)};
		
		if (eventType.equals(EventType.ZONE_BYPASSED)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_ZONE_BYPASSED), params);
		} else if (eventType.equals(EventType.ZONE_RESTORED)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_ZONE_RESTORED), params);
		} else if (eventType.equals(EventType.ALL_ZONES_RESTORED)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_ALL_ZONES_RESTORED), params);
		} else if (eventType.equals(EventType.DISARMED)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_DISARMED), params);
		} else if (eventType.equals(EventType.DAY)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_DAY), params);
		} else if (eventType.equals(EventType.NIGHT)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_NIGHT), params);
		} else if (eventType.equals(EventType.AWAY)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_AWAY), params);
		} else if (eventType.equals(EventType.VACATION)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_VACATION), params);
		} else if (eventType.equals(EventType.DAY_INSTANT)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_DAY_INSTANT), params);
		} else if (eventType.equals(EventType.NIGHT_DELAYED)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_NIGHT_DELAYED), params);
		} else if (eventType.equals(EventType.ZONE_TRIPPED)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_ZONE_TRIPPED), params);
		} else if (eventType.equals(EventType.ZONE_TROUBLE)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_ZONE_TROUBLE), params);
		} else if (eventType.equals(EventType.REMOTE_PHONE_ACCESS)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_REMOTE_PHONE_ACCESS), params);			
		} else if (eventType.equals(EventType.REMOTE_PHONE_LOCKOUT)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_REMOTE_PHONE_LOCKOUT), params);
		} else if (eventType.equals(EventType.ZONE_AUTO_BYPASSED)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_ZONE_AUTO_BYPASSED), params);
		} else if (eventType.equals(EventType.ZONE_TROUBLE_CLEARED)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_ZONE_TROUBLE_CLEARED), params);
		} else if (eventType.equals(EventType.PC_ACCESS)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_PC_ACCESS), params);
		} else if (eventType.equals(EventType.ALARM_ACTIVATED)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_ALARM_ACTIVATED), params);
		} else if (eventType.equals(EventType.ALARM_RESET)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_ALARM_RESET), params);
		} else if (eventType.equals(EventType.SYSTEM_RESET)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_SYSTEM_RESET), params);
		} else if (eventType.equals(EventType.MESSAGE_LOGGED)) {
			return MessageFormat.format(getContext().getString(R.string.EVENTLOG_MESSAGE_LOGGED), params);
		} else {
			return "";
		}
	}
}
