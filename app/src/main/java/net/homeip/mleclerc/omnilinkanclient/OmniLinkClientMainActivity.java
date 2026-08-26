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

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class OmniLinkClientMainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {

    private List<Category> categories = new ArrayList<>();
    private View[] views = new View[0];
    private Category selectedCategory;
    private LinearLayout mainLayout;
    private FrameLayout loadingOverlay;
    private CircularProgressIndicator progressIndicator;
    private AutoCompleteTextView categoryDropdown;
    private boolean useOmniLink;
    private SharedPreferences sharedPrefs;
    private ModelFactory modelFactory;
    private boolean preferencesChanged;

    private static final String CATEGORY_PROP = "category";
    private static final String OMNILINK_MODEL_PROP = "useOmniLinkModel";
    private static final String CONFIGURED_PROP = "configured";
    private static final int PREFS_UPDATED = 1;
    private static final int PREFS_CONFIGURED = 2;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private boolean justCreated = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        justCreated = true;
        setContentView(R.layout.activity_main);

        // Status bar: blue background, white icons (matches toolbar)
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.md_primary));
        WindowInsetsControllerCompat insetsCtrl =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        insetsCtrl.setAppearanceLightStatusBars(false); // false = white icons on dark blue bg

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainLayout = findViewById(R.id.mainLayout);
        categoryDropdown = findViewById(R.id.categorySpinner);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        progressIndicator = findViewById(R.id.progressIndicator);

        sharedPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
        useOmniLink = sharedPrefs.getBoolean(OMNILINK_MODEL_PROP, true);
        modelFactory = useOmniLink ? new OmniLinkModelFactory(this, sharedPrefs) : new SoapModelFactory(this);
        boolean configured = sharedPrefs.getBoolean(CONFIGURED_PROP, false);

        if (!configured && showPreferenceScreen(PREFS_CONFIGURED)) {
            // Preference screen is displayed
        } else {
            initCategories(savedInstanceState);
        }
    }

    private void initCategories(Bundle savedInstanceState) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

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

        int categoryPos = (savedInstanceState != null) ? savedInstanceState.getInt(CATEGORY_PROP, 0) : 0;

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        categoryDropdown.setAdapter(adapter);
        categoryDropdown.setOnItemClickListener((parent, view, pos, id) -> {
            Category category = (Category) parent.getItemAtPosition(pos);
            displayCategory(category, false);
        });

        // Set initial selection text and trigger load
        if (!categories.isEmpty()) {
            Category initial = categories.get(categoryPos);
            categoryDropdown.setText(initial.toString(), false);
            displayCategory(initial, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (justCreated) {
            justCreated = false;
            return; // onCreate already loaded the initial category
        }
        if (selectedCategory != null) {
            displayCategory(selectedCategory, true);
        }
    }

    @Override
    protected void onStop() {
        if (loadingOverlay != null) loadingOverlay.setVisibility(View.GONE);

        for (Category category : categories) {
            category.destroy();
        }
        modelFactory.destroy();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        // Hide Options item if no preference activity is available
        MenuItem optionsItem = menu.findItem(R.id.action_options);
        if (optionsItem != null) {
            optionsItem.setVisible(modelFactory.getPreferenceActivityClass() != null);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            displayCategory(selectedCategory, true);
            return true;
        } else if (id == R.id.action_options) {
            showPreferenceScreen(PREFS_UPDATED);
            return true;
        } else if (id == R.id.action_exit) {
            finish();
            return true;
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
            loadingOverlay.setVisibility(View.VISIBLE);
        }

        executor.execute(() -> {
            boolean success;
            try {
                if (refresh) selectedCategory.reset();
                if (!selectedCategory.isLoaded()) selectedCategory.load();
                success = true;
            } catch (ModelException ex) {
                ex.printStackTrace();
                success = false;
            }
            final boolean result = success;
            final Category loadedCategory = category;
            runOnUiThread(() -> {
                loadingOverlay.setVisibility(View.GONE);
                if (result) {
                    if (selectedCategory == loadedCategory) {
                        View[] newViews = loadedCategory.getViews();
                        for (View view : newViews) {
                            mainLayout.addView(view);
                        }
                        views = newViews;
                    }
                } else {
                    new MaterialAlertDialogBuilder(OmniLinkClientMainActivity.this)
                            .setTitle(R.string.DIALOG_ERROR_TITLE)
                            .setMessage(getString(R.string.DIALOG_ERROR_MESSAGE, loadedCategory.getName()))
                            .setNeutralButton(R.string.DIALOG_ERROR_BUTTON, (d, which) -> {
                                for (Category c : categories) c.reset();
                                displayCategory(loadedCategory, false);
                            })
                            .show();
                }
            });
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if (categoryDropdown != null && !categories.isEmpty()) {
            String selected = categoryDropdown.getText().toString();
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).toString().equals(selected)) {
                    savedInstanceState.putInt(CATEGORY_PROP, i);
                    break;
                }
            }
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
                if (preferencesChanged) restartActivity();
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

