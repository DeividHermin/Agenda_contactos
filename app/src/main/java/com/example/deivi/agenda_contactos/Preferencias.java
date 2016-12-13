package com.example.deivi.agenda_contactos;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class Preferencias extends PreferenceActivity {

    CheckBoxPreference cbGaleria, cbCamara;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences2);

        cbGaleria = (CheckBoxPreference)findPreference("cbGaleria");
        cbGaleria.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().equals("true")) {
                    cbCamara.setChecked(false);
                } else {
                    cbCamara.setChecked(true);
                }
                return true;
            }
        });
        cbCamara = (CheckBoxPreference)findPreference("cbCamara");
        cbCamara.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().equals("true")) {
                    cbGaleria.setChecked(false);
                } else {
                    cbGaleria.setChecked(true);
                }
                return true;
            }
        });
    }
}
