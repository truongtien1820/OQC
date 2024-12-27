package com.example.laprap001;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;


import java.util.Locale;

// SetLanguage.java
public class SetLanguage {
    private Context context;

    public SetLanguage(Context context) {
        this.context = context;
    }

    public void SetLanguage() {
        SharedPreferences preferences = context.getSharedPreferences("Language", Context.MODE_PRIVATE);
        int languageSetting = preferences.getInt("Language", 0);
        Locale locale;
        if (languageSetting == 0) {
            locale = new Locale("zh"); // 中文
        } else {
            locale = new Locale("vi"); // Tiếng Việt
        }
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}

