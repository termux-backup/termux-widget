package com.termux.widget.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.termux.widget.R;

public final class WidgetColors {

    public static final String PREFS_NAME = "widget_colors";
    
    public static final String KEY_HEADER_BACKGROUND = "header_background";
    public static final String KEY_HEADER_TEXT = "header_text";
    public static final String KEY_LIST_BACKGROUND = "list_background";
    public static final String KEY_DIVIDER = "divider";
    public static final String KEY_EMPTY_VIEW_BACKGROUND = "empty_view_background";
    public static final String KEY_EMPTY_VIEW_TEXT = "empty_view_text";
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_CUSTOM_COLORS_ENABLED = "custom_colors_enabled";

    public static final int NO_COLOR = Integer.MAX_VALUE;

    private WidgetColors() {
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isCustomColorsEnabled(Context context) {
        return getPreferences(context).getBoolean(KEY_CUSTOM_COLORS_ENABLED, false);
    }

    public static void setCustomColorsEnabled(Context context, boolean enabled) {
        getPreferences(context).edit().putBoolean(KEY_CUSTOM_COLORS_ENABLED, enabled).apply();
    }

    @ColorInt
    public static int getHeaderBackground(Context context) {
        return getColor(context, KEY_HEADER_BACKGROUND, R.color.widget_header_background);
    }

    public static void setHeaderBackground(Context context, @ColorInt int color) {
        saveColor(context, KEY_HEADER_BACKGROUND, color);
    }

    @ColorInt
    public static int getHeaderText(Context context) {
        return getColor(context, KEY_HEADER_TEXT, R.color.widget_header_text);
    }

    public static void setHeaderText(Context context, @ColorInt int color) {
        saveColor(context, KEY_HEADER_TEXT, color);
    }

    @ColorInt
    public static int getListBackground(Context context) {
        return getColor(context, KEY_LIST_BACKGROUND, R.color.widget_list_background);
    }

    public static void setListBackground(Context context, @ColorInt int color) {
        saveColor(context, KEY_LIST_BACKGROUND, color);
    }

    @ColorInt
    public static int getDivider(Context context) {
        return getColor(context, KEY_DIVIDER, R.color.widget_divider);
    }

    public static void setDivider(Context context, @ColorInt int color) {
        saveColor(context, KEY_DIVIDER, color);
    }

    @ColorInt
    public static int getEmptyViewBackground(Context context) {
        return getColor(context, KEY_EMPTY_VIEW_BACKGROUND, R.color.widget_empty_view_background);
    }

    public static void setEmptyViewBackground(Context context, @ColorInt int color) {
        saveColor(context, KEY_EMPTY_VIEW_BACKGROUND, color);
    }

    @ColorInt
    public static int getEmptyViewText(Context context) {
        return getColor(context, KEY_EMPTY_VIEW_TEXT, R.color.widget_empty_view_text);
    }

    public static void setEmptyViewText(Context context, @ColorInt int color) {
        saveColor(context, KEY_EMPTY_VIEW_TEXT, color);
    }

    @ColorInt
    public static int getItemText(Context context) {
        return getColor(context, KEY_ITEM_TEXT, R.color.widget_item_text);
    }

    public static void setItemText(Context context, @ColorInt int color) {
        saveColor(context, KEY_ITEM_TEXT, color);
    }

    @ColorInt
    private static int getColor(Context context, String key, int defaultResId) {
        SharedPreferences prefs = getPreferences(context);
        String colorString = prefs.getString(key, null);
        if (colorString != null && !colorString.isEmpty()) {
            try {
                return Color.parseColor(colorString);
            } catch (Exception e) {
                return context.getColor(defaultResId);
            }
        }
        return context.getColor(defaultResId);
    }

    public static void clearCustomColors(Context context) {
        getPreferences(context).edit()
            .remove(KEY_HEADER_BACKGROUND)
            .remove(KEY_HEADER_TEXT)
            .remove(KEY_LIST_BACKGROUND)
            .remove(KEY_DIVIDER)
            .remove(KEY_EMPTY_VIEW_BACKGROUND)
            .remove(KEY_EMPTY_VIEW_TEXT)
            .remove(KEY_ITEM_TEXT)
            .apply();
    }

    private static void saveColor(Context context, String key, @ColorInt int color) {
        String colorString = String.format("#%08X", color);
        getPreferences(context).edit().putString(key, colorString).apply();
    }

    public static void applyColorsToRemoteViews(Context context, RemoteViews remoteViews) {
        if (!isCustomColorsEnabled(context)) {
            return;
        }

        remoteViews.setInt(R.id.top_row, "setBackgroundColor", getHeaderBackground(context));
        remoteViews.setInt(R.id.widget_title, "setTextColor", getHeaderText(context));
        remoteViews.setInt(R.id.widget_list, "setBackgroundColor", getListBackground(context));
        remoteViews.setInt(R.id.empty_view, "setBackgroundColor", getEmptyViewBackground(context));
        remoteViews.setInt(R.id.empty_view, "setTextColor", getEmptyViewText(context));
    }

    public static void applyColorsToView(Context context, View view) {
        if (!isCustomColorsEnabled(context)) {
            return;
        }

        View headerBackground = view.findViewById(R.id.top_row);
        if (headerBackground != null) {
            headerBackground.setBackgroundColor(getHeaderBackground(context));
        }

        View listBackground = view.findViewById(R.id.widget_list);
        if (listBackground != null) {
            listBackground.setBackgroundColor(getListBackground(context));
        }

        View emptyView = view.findViewById(R.id.empty_view);
        if (emptyView != null) {
            emptyView.setBackgroundColor(getEmptyViewBackground(context));
            if (emptyView instanceof TextView) {
                ((TextView) emptyView).setTextColor(getEmptyViewText(context));
            }
        }
    }
}