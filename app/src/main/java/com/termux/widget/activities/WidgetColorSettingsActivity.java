package com.termux.widget.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.termux.shared.activity.media.AppCompatActivityUtils;
import com.termux.shared.logger.Logger;
import com.termux.shared.termux.TermuxConstants;
import com.termux.shared.theme.NightMode;
import com.termux.widget.R;
import com.termux.widget.TermuxWidgetProvider;
import com.termux.widget.utils.WidgetColors;

public class WidgetColorSettingsActivity extends AppCompatActivity {

    public static final String LOG_TAG = "WidgetColorSettingsActivity";

    private static final int REQUEST_COLOR_PICKER = 1;

    private View headerBackgroundView;
    private View headerTextView;
    private View listBackgroundView;
    private View dividerView;
    private View emptyBackgroundView;
    private View emptyTextView;
    private View itemTextView;

    private android.widget.TextView headerBackgroundText;
    private android.widget.TextView headerTextText;
    private android.widget.TextView listBackgroundText;
    private android.widget.TextView dividerText;
    private android.widget.TextView emptyBackgroundText;
    private android.widget.TextView emptyTextText;
    private android.widget.TextView itemTextText;

    private String currentColorKey;

    private static final java.util.HashMap<Integer, String> COLOR_NAMES = new java.util.HashMap<>();
    static {
        COLOR_NAMES.put(0xFFFFFFFF, "White");
        COLOR_NAMES.put(0xFF000000, "Black");
        COLOR_NAMES.put(0xFFFF0000, "Red");
        COLOR_NAMES.put(0xFF00FF00, "Green");
        COLOR_NAMES.put(0xFF0000FF, "Blue");
        COLOR_NAMES.put(0xFFFFFF00, "Yellow");
        COLOR_NAMES.put(0xFF00FFFF, "Cyan");
        COLOR_NAMES.put(0xFFFF00FF, "Magenta");
        COLOR_NAMES.put(0xFF888888, "Gray");
        COLOR_NAMES.put(0xFF444444, "Dark Gray");
        COLOR_NAMES.put(0xFFCCCCCC, "Light Gray");
        COLOR_NAMES.put(0xFFFF9800, "Orange");
        COLOR_NAMES.put(0xFFE91E63, "Pink");
        COLOR_NAMES.put(0xFF9C27B0, "Purple");
        COLOR_NAMES.put(0xFF795548, "Brown");
    }

    private static String getColorName(int color) {
        String name = COLOR_NAMES.get(color);
        if (name != null) return name;
        return String.format("#%06X", color & 0xFFFFFF);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.logDebug(LOG_TAG, "onCreate");

        setContentView(R.layout.activity_widget_color_settings);

        AppCompatActivityUtils.setNightMode(this, NightMode.getAppNightMode().getName(), true);

        AppCompatActivityUtils.setToolbar(this, com.termux.shared.R.id.toolbar);
        AppCompatActivityUtils.setToolbarTitle(this, com.termux.shared.R.id.toolbar,
                getString(R.string.settings_widget_colors), 0);

        initViews();
        loadCurrentColors();
    }

    private void initViews() {
        headerBackgroundView = findViewById(R.id.view_header_background);
        headerTextView = findViewById(R.id.view_header_text);
        listBackgroundView = findViewById(R.id.view_list_background);
        dividerView = findViewById(R.id.view_divider);
        emptyBackgroundView = findViewById(R.id.view_empty_background);
        emptyTextView = findViewById(R.id.view_empty_text);
        itemTextView = findViewById(R.id.view_item_text);

        headerBackgroundText = findViewById(R.id.text_header_background);
        headerTextText = findViewById(R.id.text_header_text);
        listBackgroundText = findViewById(R.id.text_list_background);
        dividerText = findViewById(R.id.text_divider);
        emptyBackgroundText = findViewById(R.id.text_empty_background);
        emptyTextText = findViewById(R.id.text_empty_text);
        itemTextText = findViewById(R.id.text_item_text);

        headerBackgroundView.setOnClickListener(v -> showColorPicker(WidgetColors.KEY_HEADER_BACKGROUND, headerBackgroundView));
        headerTextView.setOnClickListener(v -> showColorPicker(WidgetColors.KEY_HEADER_TEXT, headerTextView));
        listBackgroundView.setOnClickListener(v -> showColorPicker(WidgetColors.KEY_LIST_BACKGROUND, listBackgroundView));
        dividerView.setOnClickListener(v -> showColorPicker(WidgetColors.KEY_DIVIDER, dividerView));
        emptyBackgroundView.setOnClickListener(v -> showColorPicker(WidgetColors.KEY_EMPTY_VIEW_BACKGROUND, emptyBackgroundView));
        emptyTextView.setOnClickListener(v -> showColorPicker(WidgetColors.KEY_EMPTY_VIEW_TEXT, emptyTextView));
        itemTextView.setOnClickListener(v -> showColorPicker(WidgetColors.KEY_ITEM_TEXT, itemTextView));

        findViewById(R.id.switch_custom_colors).setOnClickListener(v -> {
            boolean enabled = ((com.google.android.material.switchmaterial.SwitchMaterial) v).isChecked();
            WidgetColors.setCustomColorsEnabled(this, enabled);
            updateCustomColorsContainerVisibility(enabled);
            refreshWidgets();
        });

        findViewById(R.id.button_reset_colors).setOnClickListener(v -> resetColors());
    }

    private void loadCurrentColors() {
        boolean customEnabled = WidgetColors.isCustomColorsEnabled(this);
        ((com.google.android.material.switchmaterial.SwitchMaterial) findViewById(R.id.switch_custom_colors)).setChecked(customEnabled);
        updateCustomColorsContainerVisibility(customEnabled);

        int headerBg = WidgetColors.getHeaderBackground(this);
        int headerTxt = WidgetColors.getHeaderText(this);
        int listBg = WidgetColors.getListBackground(this);
        int divider = WidgetColors.getDivider(this);
        int emptyBg = WidgetColors.getEmptyViewBackground(this);
        int emptyTxt = WidgetColors.getEmptyViewText(this);
        int itemTxt = WidgetColors.getItemText(this);

        setViewColor(headerBackgroundView, headerBg);
        setViewColor(headerTextView, headerTxt);
        setViewColor(listBackgroundView, listBg);
        setViewColor(dividerView, divider);
        setViewColor(emptyBackgroundView, emptyBg);
        setViewColor(emptyTextView, emptyTxt);
        setViewColor(itemTextView, itemTxt);

        headerBackgroundText.setText(getColorName(headerBg));
        headerTextText.setText(getColorName(headerTxt));
        listBackgroundText.setText(getColorName(listBg));
        dividerText.setText(getColorName(divider));
        emptyBackgroundText.setText(getColorName(emptyBg));
        emptyTextText.setText(getColorName(emptyTxt));
        itemTextText.setText(getColorName(itemTxt));
    }

    private void updateCustomColorsContainerVisibility(boolean visible) {
        LinearLayout container = findViewById(R.id.custom_colors_container);
        container.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setViewColor(View view, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(8);
        view.setBackground(drawable);
    }

    private void showColorPicker(String colorKey, View targetView) {
        currentColorKey = colorKey;

        String[] colorNames = {
            "White", "Black", "Red", "Green", "Blue", "Yellow", "Cyan", "Magenta",
            "Gray", "Dark Gray", "Light Gray", "Orange", "Pink", "Purple", "Brown"
        };

        int[] colorValues = {
            Color.WHITE, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
            Color.CYAN, Color.MAGENTA, Color.GRAY, Color.DKGRAY, Color.LTGRAY,
            0xFFFF9800, 0xFFE91E63, 0xFF9C27B0, 0xFF795548
        };

        new AlertDialog.Builder(this)
            .setTitle(R.string.settings_color_picker_title)
            .setItems(colorNames, (dialog, which) -> {
                int selectedColor = colorValues[which];
                applyColor(selectedColor);
                setViewColor(targetView, selectedColor);
                updateColorText(currentColorKey, selectedColor);
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    private void updateColorText(String colorKey, int color) {
        String name = getColorName(color);
        switch (colorKey) {
            case WidgetColors.KEY_HEADER_BACKGROUND:
                headerBackgroundText.setText(name);
                break;
            case WidgetColors.KEY_HEADER_TEXT:
                headerTextText.setText(name);
                break;
            case WidgetColors.KEY_LIST_BACKGROUND:
                listBackgroundText.setText(name);
                break;
            case WidgetColors.KEY_DIVIDER:
                dividerText.setText(name);
                break;
            case WidgetColors.KEY_EMPTY_VIEW_BACKGROUND:
                emptyBackgroundText.setText(name);
                break;
            case WidgetColors.KEY_EMPTY_VIEW_TEXT:
                emptyTextText.setText(name);
                break;
            case WidgetColors.KEY_ITEM_TEXT:
                itemTextText.setText(name);
                break;
        }
    }

    private void applyColor(int color) {
        if (currentColorKey == null) return;

        switch (currentColorKey) {
            case WidgetColors.KEY_HEADER_BACKGROUND:
                WidgetColors.setHeaderBackground(this, color);
                break;
            case WidgetColors.KEY_HEADER_TEXT:
                WidgetColors.setHeaderText(this, color);
                break;
            case WidgetColors.KEY_LIST_BACKGROUND:
                WidgetColors.setListBackground(this, color);
                break;
            case WidgetColors.KEY_DIVIDER:
                WidgetColors.setDivider(this, color);
                break;
            case WidgetColors.KEY_EMPTY_VIEW_BACKGROUND:
                WidgetColors.setEmptyViewBackground(this, color);
                break;
            case WidgetColors.KEY_EMPTY_VIEW_TEXT:
                WidgetColors.setEmptyViewText(this, color);
                break;
            case WidgetColors.KEY_ITEM_TEXT:
                WidgetColors.setItemText(this, color);
                break;
        }

        refreshWidgets();
    }

    private void resetColors() {
        WidgetColors.clearCustomColors(this);
        loadCurrentColors();
        refreshWidgets();
        Toast.makeText(this, R.string.settings_reset_colors, Toast.LENGTH_SHORT).show();
    }

    private void refreshWidgets() {
        TermuxWidgetProvider.sendIntentToRefreshAllWidgets(this, LOG_TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
