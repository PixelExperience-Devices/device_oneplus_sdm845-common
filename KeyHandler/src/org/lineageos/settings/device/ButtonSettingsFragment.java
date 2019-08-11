/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.device;

import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceGroup;
import android.text.TextUtils;
import android.view.MenuItem;

import org.lineageos.settings.device.utils.Constants;

public class ButtonSettingsFragment extends PreferenceFragment
        implements OnPreferenceChangeListener {

    public static final String KEY_VIBSTRENGTH = "vib_strength";
    private VibratorStrengthPreference mVibratorStrength;

    public static final String KEY_CALL_VIBSTRENGTH = "vib_call_strength";
    private VibratorCallStrengthPreference mVibratorCallStrength;

    public static final String KEY_NOTIF_VIBSTRENGTH = "vib_notif_strength";
    private VibratorNotifStrengthPreference mVibratorNotifStrength;

    private ListPreference mTopKeyPref;
    private ListPreference mMiddleKeyPref;
    private ListPreference mBottomKeyPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.button_panel);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        mVibratorStrength = (VibratorStrengthPreference) findPreference(KEY_VIBSTRENGTH);
        if (mVibratorStrength != null) {
            mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());
        }

        mVibratorCallStrength = (VibratorCallStrengthPreference) findPreference(KEY_CALL_VIBSTRENGTH);
        if (mVibratorCallStrength != null) {
            mVibratorCallStrength.setEnabled(VibratorCallStrengthPreference.isSupported());
        }

        mVibratorNotifStrength = (VibratorNotifStrengthPreference) findPreference(KEY_NOTIF_VIBSTRENGTH);
        if (mVibratorNotifStrength != null) {
            mVibratorNotifStrength.setEnabled(VibratorNotifStrengthPreference.isSupported());
        }

        mTopKeyPref = (ListPreference) findPreference(Constants.NOTIF_SLIDER_TOP_KEY);
        mTopKeyPref.setValueIndex(Constants.getPreferenceInt(getContext(), Constants.NOTIF_SLIDER_TOP_KEY));
        mTopKeyPref.setOnPreferenceChangeListener(this);
        mMiddleKeyPref = (ListPreference) findPreference(Constants.NOTIF_SLIDER_MIDDLE_KEY);
        mMiddleKeyPref.setValueIndex(Constants.getPreferenceInt(getContext(), Constants.NOTIF_SLIDER_MIDDLE_KEY));
        mMiddleKeyPref.setOnPreferenceChangeListener(this);
        mBottomKeyPref = (ListPreference) findPreference(Constants.NOTIF_SLIDER_BOTTOM_KEY);
        mBottomKeyPref.setValueIndex(Constants.getPreferenceInt(getContext(), Constants.NOTIF_SLIDER_BOTTOM_KEY));
        mBottomKeyPref.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Constants.setPreferenceInt(getContext(), preference.getKey(), Integer.parseInt((String) newValue));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
