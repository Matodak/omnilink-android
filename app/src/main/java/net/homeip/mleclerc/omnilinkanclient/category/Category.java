package net.homeip.mleclerc.omnilinkanclient.category;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.util.MySpinner;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class Category {
	protected abstract class Execution<T> {
		public void preExecute() {
		}

		public abstract boolean execute(T selection) throws ModelException;
		
		public void postExecute(boolean success) {
		}
		
		public boolean isDisplayToast() {
			return true;
		}
	}

	public final static int DATE_LONG = 1;
	public final static int TIME_LONG = 2;
	
	private Context context;
	private ProgressDialog progressDialog;
	private int buttonHeight;
	
	protected Category(Context context, DisplayMetrics displayMetrics) {
		this.context = context;
		this.buttonHeight = 0;
		if (displayMetrics.density == 4.0)
		{
			buttonHeight = 128;
		}
		else
		{
			buttonHeight = 72;
		}
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getString(R.string.EXECUTING));
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
	}
	
	public abstract boolean isLoaded();
	
	public abstract String getName();

	public abstract View[] getViews();

	public abstract void reset();

	public abstract void load() throws ModelException;
	
	public void destroy()
	{
		progressDialog.dismiss();
	}
	
	protected Context getContext() {
		return context;
	}

	public String toString() {
		return getName();
	}
	
	protected View createDateView(String label, long date, int format) {
		RelativeLayout layout = new RelativeLayout(getContext());

		TextView labelView = new TextView(getContext());
		RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		labelLayoutParams.setMargins(0, 25, 0, 25);
		labelView.setLayoutParams(labelLayoutParams);
		labelView.setText(label);
		layout.addView(labelView);

		TextView dateTimeView = new TextView(getContext());
		RelativeLayout.LayoutParams dateTimeLayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		dateTimeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		dateTimeLayoutParams.setMargins(0, 25, 0, 25);
		dateTimeView.setLayoutParams(dateTimeLayoutParams);
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(date);
		StringBuffer strbuf = new StringBuffer();
		if ((format & DATE_LONG) == DATE_LONG) {
			strbuf.append("EEE, MMM d, yyyy ");
		} 
		if ((format & TIME_LONG) == TIME_LONG) {
			strbuf.append("hh:mm:ss a");
		}
		SimpleDateFormat df = new SimpleDateFormat(strbuf.toString(), Locale.getDefault());
		dateTimeView.setText(df.format(calendar.getTime()));
		layout.addView(dateTimeView);

		return layout;
	}

	protected View createLabelAndTextView(String label, String text) {
		RelativeLayout layout = new RelativeLayout(getContext());

		TextView labelView = new TextView(getContext());
		RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		labelLayoutParams.setMargins(0, 25, 0, 25);
		labelView.setLayoutParams(labelLayoutParams);
		labelView.setText(label);
		layout.addView(labelView);

		TextView textView = new TextView(getContext());
		RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		textLayoutParams.setMargins(0, 25, 0, 25);
		textLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		textView.setLayoutParams(textLayoutParams);
		textView.setText(text);
		layout.addView(textView);

		return layout;
	}

	protected void setLabelAndTextView(View view, String text) {
		RelativeLayout layout = (RelativeLayout) view;
		
		TextView textView = (TextView) layout.getChildAt(1);
		textView.setText(text);
	}
	
	protected View createLabelAndChoiceView(String label, Object[] choices, Object initialChoice, Execution execution) {
		RelativeLayout layout = new RelativeLayout(getContext());

		TextView labelView = new TextView(getContext());
		labelView.setText(label);
		RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		labelLayoutParams.setMargins(0, 25, 0, 25);
		labelView.setLayoutParams(labelLayoutParams);
		layout.addView(labelView);

		ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(getContext(), android.R.layout.simple_spinner_item,
				choices);
		MySpinner spinner = new MySpinner(getContext());
		spinner.setAdapter(adapter);
		RelativeLayout.LayoutParams spinnerLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		spinnerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		spinner.setLayoutParams(spinnerLayoutParams);
		if (initialChoice != null) {
			int initialChoicePos = adapter.getPosition(initialChoice);
			spinner.setSelection(initialChoicePos);
			spinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListenerImpl(initialChoicePos, execution));
		}
		layout.addView(spinner);

		return layout;
	}

	protected void setLabelAndChoiceView(View view, Object choice) {
		RelativeLayout layout = (RelativeLayout) view;
		
		MySpinner spinner = (MySpinner) layout.getChildAt(1);
		@SuppressWarnings("unchecked")
		ArrayAdapter<Object> adapter = (ArrayAdapter<Object>) spinner.getAdapter();
		int choicePos = adapter.getPosition(choice);
		spinner.setSelectedItemPosition(choicePos);
	}
	
	protected View createLabelAndButtonView(String label, String buttonText, Execution execution) {
		RelativeLayout layout = new RelativeLayout(getContext());

		TextView labelView = new TextView(getContext());
		labelView.setText(label);
		RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		labelLayoutParams.setMargins(0, 25, 0, 25);
		labelView.setLayoutParams(labelLayoutParams);
		layout.addView(labelView);

		Button button = new Button(getContext());
		button.setText(buttonText);
		button.setIncludeFontPadding(false);
		RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, buttonHeight);
		buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		button.setLayoutParams(buttonLayoutParams);
		button.setOnClickListener(new ButtonOnClickListenerImpl(execution));
		layout.addView(button);

		return layout;
	}

	protected void setLabelAndButtonView(View view, String buttonText) {
		RelativeLayout layout = (RelativeLayout) view;
		
		Button button = (Button) layout.getChildAt(1);
		button.setText(buttonText);
	}
	
	protected View createLabelAndTwoButtonsView(String label, String buttonText1, Execution execution1, String buttonText2,
			Execution execution2) {
		RelativeLayout layout = new RelativeLayout(getContext());

		TextView labelView = new TextView(getContext());
		labelView.setText(label);
		RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		labelLayoutParams.setMargins(0, 25, 0, 25);
		labelView.setLayoutParams(labelLayoutParams);
		layout.addView(labelView);

		LinearLayout buttonLayout = new LinearLayout(getContext());
		RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		buttonLayout.setLayoutParams(buttonLayoutParams);
		layout.addView(buttonLayout);

		Button button1 = new Button(getContext());
		button1.setText(buttonText1);
		button1.setIncludeFontPadding(false);
		LinearLayout.LayoutParams buttonLayoutParams1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, buttonHeight);
		button1.setLayoutParams(buttonLayoutParams1);
		button1.setOnClickListener(new ButtonOnClickListenerImpl(execution1));
		buttonLayout.addView(button1);

		Button button2 = new Button(getContext());
		button2.setText(buttonText2);
		button2.setIncludeFontPadding(false);
		LinearLayout.LayoutParams buttonLayoutParams2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, buttonHeight);
		button2.setLayoutParams(buttonLayoutParams2);
		button2.setOnClickListener(new ButtonOnClickListenerImpl(execution2));
		buttonLayout.addView(button2);

		return layout;
	}

	protected View createCheckBoxAndTextView(String label, boolean checked, Execution execution, String text) {
		RelativeLayout layout = new RelativeLayout(getContext());

		CheckBox checkBox = new CheckBox(getContext());
		checkBox.setText(label);
		checkBox.setChecked(checked);
		checkBox.setOnCheckedChangeListener(new CheckBoxOnCheckedListenerImpl(execution));
		layout.addView(checkBox);

		TextView labelView = new TextView(getContext());
		labelView.setText(text);
		RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		labelLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		labelLayoutParams.setMargins(0, 25, 0, 25);
		labelView.setLayoutParams(labelLayoutParams);
		layout.addView(labelView);

		return layout;
	}

	protected void setCheckBoxAndTextView(View view, boolean check, String text) {
		RelativeLayout layout = (RelativeLayout) view;
		
		CheckBox checkBox = (CheckBox) layout.getChildAt(0);
		checkBox.setChecked(check);
		
		TextView labelView = (TextView) layout.getChildAt(1);
		labelView.setText(text);
	}
	
	class BaseListenerImpl {
		protected Execution execution;

		protected BaseListenerImpl(Execution execution) {
			this.execution = execution;
		}

		protected void displayToast(boolean success) {
			int resourceKey = success ? R.string.EXECUTION_SUCCESS : R.string.EXECUTION_FAILURE;
			Toast.makeText(getContext(), resourceKey, Toast.LENGTH_SHORT).show();
		}
	}

	class ButtonOnClickListenerImpl extends BaseListenerImpl implements OnClickListener {
		public ButtonOnClickListenerImpl(Execution execution) {
			super(execution);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View view) {
			final Button button = (Button) view;
			AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
				@Override
				protected void onPreExecute() {
					progressDialog.show();					
					execution.preExecute();
				}
				
				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						String buttonText = button.getText().toString();
						return execution.execute(buttonText);
					} catch (ModelException e) {
						return false;
					}
				}

				@Override
				protected void onPostExecute(Boolean success) {
					execution.postExecute(success);
					progressDialog.dismiss();
					if (execution.isDisplayToast()) {
						displayToast(success);
					}
				}
			};
			task.execute();
		}
	};

	class SpinnerOnItemSelectedListenerImpl extends BaseListenerImpl implements OnItemSelectedListener {
		private int lastPos;

		public SpinnerOnItemSelectedListenerImpl(int pos, Execution execution) {
			super(execution);

			lastPos = pos;
		}

		@Override
		public void onItemSelected(final AdapterView<?> parent, View view, final int pos, long id) {
			// Make sure the change is done by the user and not programmatically
			MySpinner spinner = (MySpinner) parent;
			if (spinner.isSelectionSetManually()) {
				spinner.clearSelectionSetManually();
				lastPos = pos;
				return;
			}
			
			if (pos != lastPos) {
				final Object selection = parent.getItemAtPosition(pos);
				AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
					@Override
					protected void onPreExecute() {
						progressDialog.show();
						execution.preExecute();
					}

					@SuppressWarnings("unchecked")
					@Override
					protected Boolean doInBackground(Void... params) {
						try {
							return execution.execute(selection);
						} catch (ModelException ex) {
							return false;
						}
					}

					@Override
					protected void onPostExecute(Boolean success) {
						execution.postExecute(success);
						progressDialog.dismiss();
						if (execution.isDisplayToast()) {
							displayToast(success);
						}
						if (success) {
							lastPos = pos;
						} else {
							parent.setSelection(lastPos);
						}
					}
				};
				task.execute();
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	class CheckBoxOnCheckedListenerImpl extends BaseListenerImpl implements OnCheckedChangeListener {
		public CheckBoxOnCheckedListenerImpl(Execution execution) {
			super(execution);
		}

		@Override
		public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
			// Make sure the change is done by the user and not programmatically
			if (!buttonView.isPressed()) {
				return;
			}
			
			AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
				@Override
				protected void onPreExecute() {
					progressDialog.show();					
					execution.preExecute();
				}

				@SuppressWarnings("unchecked")
				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						return execution.execute(isChecked);
					} catch (ModelException ex) {
						return false;
					}
				}

				@Override
				protected void onPostExecute(Boolean success) {
					execution.postExecute(success);
					progressDialog.dismiss();
					if (execution.isDisplayToast()) {
						displayToast(success);
					}
					if (!success) {
						buttonView.setChecked(!isChecked);
					}
				}
			};
			task.execute();
		}
	};
}
