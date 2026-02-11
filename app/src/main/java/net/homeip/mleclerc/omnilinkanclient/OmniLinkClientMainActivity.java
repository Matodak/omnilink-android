package net.homeip.mleclerc.omnilinkanclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.homeip.mleclerc.omnilinkanclient.category.ButtonCategory;
import net.homeip.mleclerc.omnilinkanclient.category.Category;
import net.homeip.mleclerc.omnilinkanclient.category.InformationCategory;
import net.homeip.mleclerc.omnilinkanclient.category.MessageCategory;
import net.homeip.mleclerc.omnilinkanclient.category.SystemCategory;
import net.homeip.mleclerc.omnilinkanclient.category.ThermostatCategory;
import net.homeip.mleclerc.omnilinkanclient.category.UnitCategory;
import net.homeip.mleclerc.omnilinkanclient.category.ZoneCategory;
import net.homeip.mleclerc.omnilinkanclient.model.ButtonModel;
import net.homeip.mleclerc.omnilinkanclient.model.InformationModel;
import net.homeip.mleclerc.omnilinkanclient.model.MessageModel;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import net.homeip.mleclerc.omnilinkanclient.model.ModelFactory;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModel;
import net.homeip.mleclerc.omnilinkanclient.model.ThermostatModel;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModel;
import net.homeip.mleclerc.omnilinkanclient.model.ZoneModel;
import net.homeip.mleclerc.omnilinkanclient.model.omnilink.OmniLinkModelFactory;
import net.homeip.mleclerc.omnilinkanclient.model.soap.SoapModelFactory;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

public class OmniLinkClientMainActivity extends Activity implements OnItemSelectedListener, OnSharedPreferenceChangeListener {
	private List<Category> categories = new ArrayList<Category>();
	private View[] views = new View[0];
	private Category selectedCategory;
	private LinearLayout mainLayout;
	private ProgressDialog progressDialog;
	private Spinner spinner;
	private boolean useOmniLink;
	private SharedPreferences sharedPrefs;
	private ModelFactory modelFactory;
	private boolean preferencesChanged;
	
	private final static String CATEGORY_PROP = "category";
	private final static String OMNILINK_MODEL_PROP = "useOmniLinkModel";
	private final static String CONFIGURED_PROP = "configured";
	private final static int MENU_REFRESH = Menu.FIRST;
	private final static int MENU_MODEL = Menu.FIRST + 1;
	private final static int MENU_OPTIONS = Menu.FIRST + 2;
	private final static int MENU_EXIT = Menu.FIRST + 3;
	private final static int PREFS_UPDATED = 1;
	private final static int PREFS_CONFIGURED = 2;
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(OmniLinkClientMainActivity.this.getString(R.string.LOADING));
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);

		sharedPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
		sharedPrefs.registerOnSharedPreferenceChangeListener(this);
		useOmniLink = sharedPrefs.getBoolean(OMNILINK_MODEL_PROP, true);
		modelFactory = useOmniLink ? new OmniLinkModelFactory(this, sharedPrefs) : new SoapModelFactory(this);
		boolean configured = sharedPrefs.getBoolean(CONFIGURED_PROP, false);
		
		if (!configured && showPreferenceScreen(PREFS_CONFIGURED)) {
			// Preference screen is displayed
		} else {
			SystemModel systemModel = modelFactory.createSystemModel();
			ThermostatModel thermostatModel = modelFactory.createThermostatModel();
			MessageModel messageModel = modelFactory.createMessageModel();
			ButtonModel buttonModel = modelFactory.createButtonModel();
			UnitModel unitModel = modelFactory.createUnitModel();
			ZoneModel zoneModel = modelFactory.createZoneModel();
			InformationModel infoModel = modelFactory.createInformationModel();
	
			addCategory(new SystemCategory(this, systemModel, displayMetrics, executor));
			addCategory(new ThermostatCategory(this, thermostatModel, displayMetrics, executor));
			addCategory(new MessageCategory(this, messageModel, infoModel, displayMetrics, executor));
			addCategory(new ButtonCategory(this, buttonModel, displayMetrics, executor));
			addCategory(new UnitCategory(this, unitModel, displayMetrics, executor));
			addCategory(new ZoneCategory(this, zoneModel, displayMetrics, executor));
			addCategory(new InformationCategory(this, infoModel, systemModel, displayMetrics, executor));
	
			// Get the category to display
			int categoryPos =  (savedInstanceState != null) ? savedInstanceState.getInt(CATEGORY_PROP, 0) : 0;

			ScrollView mainView = new ScrollView(this);
			mainLayout = new LinearLayout(this);
            int topPadding = 0;
            int bottomPadding = 0;
            if (displayMetrics.density == 2.625)
                if (displayMetrics.heightPixels <= 2400) {
                    // Pixel 7
                    topPadding = 275;
                    bottomPadding = 100;
                }
                else {
                    // Pixel 9a
                    topPadding = 300;
                    bottomPadding = 150;
                }
            else {
                // Samsung tablet
                topPadding = 100;
                bottomPadding = 0;
            }
            mainLayout.setPadding(0, topPadding, 0, bottomPadding);
            mainLayout.setOrientation(LinearLayout.VERTICAL);
			ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item,
					categories);
			spinner = new Spinner(this);
			spinner.setAdapter(adapter);
			spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, 0));
			spinner.setSelection(categoryPos);
			spinner.setOnItemSelectedListener(this);
			mainLayout.addView(spinner);
			View separator = new View(this);
			separator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
			separator.setBackgroundColor(Color.DKGRAY);
			mainLayout.addView(separator);
			mainView.addView(mainLayout);
			setContentView(mainView);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		if (selectedCategory != null)
		{
			displayCategory(selectedCategory, true);
		}
	}

	@Override
	protected void onStop() {
		progressDialog.dismiss();

		for (Category category : categories) {
			category.destroy();
		}

		modelFactory.destroy();
		
		super.onStop();		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_REFRESH, 0, getString(R.string.MENU_CATEGORY_REFRESH)).setIcon(android.R.drawable.ic_menu_add);
		//menu.add(0, MENU_MODEL, 0, getString(useOmniLink ? R.string.MODEL_SOAP : R.string.MODEL_OMNILINK)).setIcon(android.R.drawable.ic_menu_add);
		if (modelFactory.getPreferenceActivityClass() != null) {
			menu.add(0, MENU_OPTIONS, 0, getString(R.string.MENU_OPTIONS)).setIcon(android.R.drawable.ic_menu_add);
		}
		menu.add(0, MENU_EXIT, 0, getString(R.string.MENU_EXIT)).setIcon(android.R.drawable.ic_menu_close_clear_cancel);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_MODEL:
		{
			// Store the new model to use
			useOmniLink = !useOmniLink;
	        sharedPrefs.edit().putBoolean(OMNILINK_MODEL_PROP, useOmniLink).apply();
	        
	        // Restart activity
	        restartActivity();
			break;
		}
		case MENU_REFRESH:
		{
			displayCategory(selectedCategory, true);
			break;
		}
		case MENU_OPTIONS:
		{
			showPreferenceScreen(PREFS_UPDATED);
			break;
		}
		case MENU_EXIT:
		{
			finish();
			break;
		}
		}

		return super.onOptionsItemSelected(item);
	}

	private void displayCategory(final Category category, final boolean refresh) {
		selectedCategory = category;

		for (View view : views) {
			mainLayout.removeView(view);
		}
		views = new View[0];
		if (refresh || !selectedCategory.isLoaded()) {
			progressDialog.show();
		}

		executor.execute(() -> {
			boolean success;
			try {
				if (refresh) {
					selectedCategory.reset();
				}
				if (!selectedCategory.isLoaded()) {
					selectedCategory.load();
				}
				success = true;
			} catch (ModelException ex) {
				ex.printStackTrace();
				success = false;
			}
			final boolean result = success;
			final Category loadedCategory = category;
			runOnUiThread(() -> {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				if (result) {
					if (selectedCategory == loadedCategory) {
						View[] newViews = loadedCategory.getViews();
						for (View view : newViews) {
							mainLayout.addView(view);
						}
						views = newViews;
					}
				} else {
					AlertDialog dialog = new AlertDialog.Builder(OmniLinkClientMainActivity.this).create();
					dialog.setTitle(R.string.DIALOG_ERROR_TITLE);
					dialog.setMessage(getString(R.string.DIALOG_ERROR_MESSAGE, loadedCategory.getName()));
					dialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.DIALOG_ERROR_BUTTON),
							(d, which) -> {
								for (Category c : categories) {
									c.reset();
								}
								displayCategory(loadedCategory, false);
							});
					dialog.show();
				}
			});
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Category category = (Category) parent.getItemAtPosition(pos);
		displayCategory(category, false);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		if (spinner != null) {
			int selectedCategoryPos = spinner.getSelectedItemPosition();
			savedInstanceState.putInt(CATEGORY_PROP, selectedCategoryPos);
		}
		
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		preferencesChanged = true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PREFS_UPDATED:
			if (preferencesChanged) {
				restartActivity();
			}
			break;
		case PREFS_CONFIGURED:
			if (preferencesChanged) {
				sharedPrefs.edit().putBoolean(CONFIGURED_PROP, true).apply();
				restartActivity();
			} else {
				finish();
			}
			break;
		}
	}

	private boolean showPreferenceScreen(int requestCode) {
		Class preferenceActivityClass = modelFactory.getPreferenceActivityClass();
		if (preferenceActivityClass != null) {
			preferencesChanged = false;
			Intent intent = new Intent(this, preferenceActivityClass);
			startActivityForResult(intent, requestCode);
			return true;
		}
		return false;
	}
	
	private void restartActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}
	
	private void addCategory(Category category) {
		categories.add(category);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		executor.shutdown();
	}
}
