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
import android.widget.Button;
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

    private final List<Category> categories = new ArrayList<>();
    private final List<Button> categoryButtons = new ArrayList<>();
    private View[] views = new View[0];
    private Category selectedCategory;
    private LinearLayout mainLayout;
    private LinearLayout categoryTabContainer;
    private FrameLayout loadingOverlay;
    private CircularProgressIndicator progressIndicator;
    private MaterialToolbar toolbar;
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

        // Status bar follows the page-header blue from Web-Link skin.
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.md_web_page_header_bg));
        WindowInsetsControllerCompat insetsCtrl =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        insetsCtrl.setAppearanceLightStatusBars(false); // false = white icons on dark blue bg

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainLayout = findViewById(R.id.mainLayout);
        categoryTabContainer = findViewById(R.id.categoryTabContainer);
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

        addCategory(new SystemCategory(this, systemModel, thermostatModel, displayMetrics, executor));
        addCategory(new ThermostatCategory(this, thermostatModel, displayMetrics, executor));
        addCategory(new ButtonCategory(this, buttonModel, displayMetrics, executor));
        addCategory(new MessageCategory(this, messageModel, infoModel, displayMetrics, executor));
        addCategory(new UnitCategory(this, unitModel, displayMetrics, executor));
        addCategory(new ZoneCategory(this, zoneModel, systemModel, displayMetrics, executor));
        addCategory(new InformationCategory(this, infoModel, systemModel, displayMetrics, executor));

        int categoryPos = (savedInstanceState != null) ? savedInstanceState.getInt(CATEGORY_PROP, 0) : 0;

        setupCategoryTabs();

        // Set initial selection text and trigger load
        if (!categories.isEmpty()) {
            Category initial = categories.get(categoryPos);
            displayCategory(initial, false);
        }
    }

    private void setupCategoryTabs() {
        categoryButtons.clear();
        categoryTabContainer.removeAllViews();

        for (int i = 0; i < categories.size(); i++) {
            final int categoryIndex = i;
            Category category = categories.get(i);
            Button button = new Button(this);
            button.setText(category.toString());
            button.setAllCaps(false);
            button.setTextSize(12f);
            button.setMinHeight(0);
            button.setMinimumHeight(0);
            button.setBackgroundResource(R.drawable.weblink_tab_unselected);
            button.setTextColor(ContextCompat.getColor(this, R.color.md_web_tab_unselected_text));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, dp(4), 0);
            button.setLayoutParams(params);
            button.setPadding(dp(8), dp(4), dp(8), dp(4));

            button.setOnClickListener(v -> displayCategory(categories.get(categoryIndex), false));
            categoryTabContainer.addView(button);
            categoryButtons.add(button);
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
        highlightSelectedCategory();

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

    private void highlightSelectedCategory() {
        if (selectedCategory == null) return;
        if (toolbar != null) {
            toolbar.setSubtitle(selectedCategory.getName());
        }
        for (int i = 0; i < categories.size() && i < categoryButtons.size(); i++) {
            Button button = categoryButtons.get(i);
            boolean selected = categories.get(i) == selectedCategory;
            button.setBackgroundResource(selected ? R.drawable.weblink_tab_selected : R.drawable.weblink_tab_unselected);
            button.setTextColor(ContextCompat.getColor(this,
                    selected ? R.color.md_web_tab_selected_text : R.color.md_web_tab_unselected_text));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if (selectedCategory != null) {
            int selectedIndex = categories.indexOf(selectedCategory);
            if (selectedIndex >= 0) {
                savedInstanceState.putInt(CATEGORY_PROP, selectedIndex);
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

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
