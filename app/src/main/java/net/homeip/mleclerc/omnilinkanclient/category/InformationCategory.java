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

import java.util.concurrent.ExecutorService;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class InformationCategory extends Category {
	private static EnumLabel[] SYSTEM_TYPES;

	private InformationModel infoModel;
	private SystemModel systemModel;
	private Dialog eventLogDialog;

	public InformationCategory(Context context, InformationModel infoModel, SystemModel systemModel, DisplayMetrics displayMetrics, ExecutorService executor) {
		super(context, displayMetrics, executor);

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
		java.util.ArrayList<View> views = new java.util.ArrayList<View>();
		int count = systemModel.getEventLogCount();
		for (int i = 0; i < count; i++) {
			String date = android.text.format.DateFormat.format(
					"MM/dd 'at' h:mm a", systemModel.getEventLogDate(i)).toString();
			views.add(createLabelAndTextView(date, getEventLogDescription(i)));
		}
		return views.toArray(new View[0]);
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
		systemModel.loadEventLog(true);
	}

	private void displayEventLog(int eventLogCount) {
		ScrollView eventLogView = new ScrollView(getContext());
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(dp(12), dp(10), dp(12), dp(10));
		layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_web_background));
		for (int i = 0; i < eventLogCount; i++) {
			long eventLogEntryDate = systemModel.getEventLogDate(i);
			String eventLogEntryText = getEventLogDescription(i);
			Calendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(eventLogEntryDate);
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()); 
			TextView eventLogEntryDateView = new TextView(getContext());
			eventLogEntryDateView.setText(df.format(calendar.getTime()));
			eventLogEntryDateView.setTextColor(ContextCompat.getColor(getContext(), R.color.md_web_header_text));
			eventLogEntryDateView.setTextSize(13f);
			layout.addView(eventLogEntryDateView);
			TextView eventLogTextView = new TextView(getContext());
			eventLogTextView.setText(eventLogEntryText);
			eventLogTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.md_web_value_text));
			eventLogTextView.setTextSize(14f);
			eventLogTextView.setPadding(0, 0, 0, dp(10));
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
