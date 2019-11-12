package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class SharedPrefManager {
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    @SuppressLint("StaticFieldLeak")
    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    void setLang(String lang) {
        SharedPreferences preferences = mCtx.getSharedPreferences(SELECTED_LANGUAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, lang);
        editor.apply();
    }

    public String getLang() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SELECTED_LANGUAGE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SELECTED_LANGUAGE, Locale.getDefault().getLanguage());
    }
}
