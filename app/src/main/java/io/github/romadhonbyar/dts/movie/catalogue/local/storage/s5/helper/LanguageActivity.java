package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.helper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Objects;

import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.MainActivity;
import io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.R;


public class LanguageActivity extends AppCompatActivity {

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String lang = SharedPrefManager.getInstance(this).getLang();
        outState.putString("message", lang);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        if (savedInstanceState != null) {
            String message = savedInstanceState.getString("message");
            LocaleHelper.setLocale(LanguageActivity.this, message);

            SharedPrefManager.getInstance(this).setLang(message);
        } else {
            String lang = SharedPrefManager.getInstance(this).getLang();
            LocaleHelper.setLocale(LanguageActivity.this, lang);

            SharedPrefManager.getInstance(this).setLang(lang);
        }

        setTitle(R.string.menu_language_settings);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        RadioButton rb_g1 = findViewById(R.id.radio_indonesian);
        RadioButton rb_g2 = findViewById(R.id.radio_english);

        String lang = SharedPrefManager.getInstance(this).getLang();

        if (Objects.equals(lang, "in")) {
            rb_g1.setChecked(true);
            Log.e("1 LanguageActivity", "01 - " + lang + " - " + Locale.getDefault().getLanguage());
        } else {
            rb_g2.setChecked(true);
            Log.e("1 LanguageActivity", "02 - " + lang + " - " + Locale.getDefault().getLanguage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String languagePref = "in";

        switch (view.getId()) {
            case R.id.radio_indonesian:
                if (checked)
                    languagePref = "in";
                break;
            case R.id.radio_english:
                if (checked)
                    languagePref = "en";
                break;
        }

        reload(languagePref);
    }

    public void reload(String languagePref) {
        LocaleHelper.setLocale(LanguageActivity.this, languagePref);
        recreate();

        SharedPrefManager.getInstance(this).setLang(languagePref);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }
}