package com.atomtex.smartterminal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**Version 1.0*/
public class ThemeUtils {
    private static int sTheme;
    public final static int THEME_LIGHT = 1;
    public final static int THEME_DARK = 3;
    public static final String PREF_THEME = "theme";

    /**Для варианта смены темы из активити (по тапу по кнопке, например)*/
    @SuppressWarnings("unused")
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        savePref(PREF_THEME, sTheme);
        activity.recreate();
    }

    /**Для варианта смены темы из настроек*/
    @SuppressWarnings("unused")
    public static void changeToThemePref(Activity activity, int theme)
    {
        sTheme = theme;
        activity.recreate();
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        int sTheme = getPrefInt(PREF_THEME, THEME_LIGHT);
        switch (sTheme)
        {
            default:
            case THEME_DARK: activity.setTheme(R.style.Theme_SmartTerminal); break;
            case THEME_LIGHT: activity.setTheme(R.style.Theme_SmartTerminal_Dark); break;
        }
    }

    @SuppressWarnings("unused")
    public static int getTheme() {
        return sTheme;
    }

//--------------------------------------------------------------------------------------------------

    static SharedPreferences mPrefManager = PreferenceManager.getDefaultSharedPreferences(App.getContext());

    public static void savePref(String key, int param) {
        mPrefManager.edit().putInt(key, param).apply();
    }

    public static int getPrefInt(String key, int defValue) {
        if (mPrefManager.contains(key)) return Integer.parseInt(mPrefManager.getString(key, String.valueOf(defValue)));
        return defValue;
    }

}
