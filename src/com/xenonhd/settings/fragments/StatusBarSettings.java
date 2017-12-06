package com.xenonhd.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.xenonhd.settings.preferences.MasterSwitchPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatusBarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String KEY_CUSTOM_CARRIER_LABEL = "custom_carrier_label";

    MasterSwitchPreference mBatteryBar;
    private Preference mCustomCarrierLabel;
    private String mCustomCarrierLabelText;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.xenonhd_settings_statusbar);

        PreferenceScreen prefSet = getPreferenceScreen();

        mBatteryBar = (MasterSwitchPreference)
                findPreference(Settings.System.STATUSBAR_BATTERY_BAR);

        mCustomCarrierLabel = (Preference) findPreference(KEY_CUSTOM_CARRIER_LABEL);
        updateCustomLabelTextSummary();
    }

    private void updateCustomLabelTextSummary() {
        mCustomCarrierLabelText = Settings.System.getString(
                getActivity().getContentResolver(), Settings.System.CUSTOM_CARRIER_LABEL);

        if (TextUtils.isEmpty(mCustomCarrierLabelText)) {
            mCustomCarrierLabel.setSummary(R.string.custom_carrier_label_notset);
        } else {
            mCustomCarrierLabel.setSummary(mCustomCarrierLabelText);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {

        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.XENONHD;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBatteryBar.reloadValue();
    }

    @Override
    public boolean onPreferenceTreeClick(final Preference preference) {
        super.onPreferenceTreeClick(preference);
        final ContentResolver resolver = getActivity().getContentResolver();
        if (preference.getKey().equals(KEY_CUSTOM_CARRIER_LABEL)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle(R.string.custom_carrier_label_title);
            alert.setMessage(R.string.custom_carrier_label_explain);

            // Set an EditText view to get user input
            final EditText input = new EditText(getActivity());
            input.setText(TextUtils.isEmpty(mCustomCarrierLabelText) ? "" : mCustomCarrierLabelText);
            input.setSelection(input.getText().length());
            alert.setView(input);
            alert.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = ((Spannable) input.getText()).toString().trim();
                            Settings.System.putString(resolver, Settings.System.CUSTOM_CARRIER_LABEL, value);
                            updateCustomLabelTextSummary();
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_CUSTOM_CARRIER_LABEL_CHANGED);
                            getActivity().sendBroadcast(i);
                        }
                    });
            alert.setNegativeButton(getString(android.R.string.cancel), null);
            alert.show();
        }
        return true;
    }
}
