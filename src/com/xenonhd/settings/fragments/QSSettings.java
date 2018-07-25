package com.xenonhd.settings.fragments;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.Settings.Secure;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import android.text.TextUtils;
import android.view.View;

import com.xenonhd.settings.preferences.SeekBarPreferenceCham;

import com.android.internal.logging.nano.MetricsProto;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

public class QSSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private SeekBarPreferenceCham mSysuiQqsCount;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.xenonhd_qs);

        PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        int value = Settings.Secure.getInt(resolver, Settings.Secure.QQS_COUNT, 6);
        mSysuiQqsCount = (SeekBarPreferenceCham) findPreference("sysui_qqs_count");
        mSysuiQqsCount.setValue(value);
        mSysuiQqsCount.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
         ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mSysuiQqsCount) {
            int val = (Integer) newValue;
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.QQS_COUNT, val, UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.XENONHD;
    }
}
