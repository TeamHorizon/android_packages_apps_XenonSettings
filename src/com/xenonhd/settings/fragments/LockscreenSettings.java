package com.xenonhd.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;

import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class LockscreenSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String FP_SUCCESS_VIBRATION = "fingerprint_success_vib";
    private static final String FP_UNLOCK_KEYSTORE = "fp_unlock_keystore";

    private FingerprintManager mFingerprintManager;
    private SwitchPreference mFingerprintVib;
    private SwitchPreference mFpKeystore;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.xenonhd_lockscreen);

        ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources resources = getResources();

        try {
            mFingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
        } catch (Exception e) {
            //ignore
        }

        // Fingerprint vibration & keystore unlock
        mFingerprintVib = (SwitchPreference) prefScreen.findPreference(FP_SUCCESS_VIBRATION);
        mFpKeystore = (SwitchPreference) prefScreen.findPreference(FP_UNLOCK_KEYSTORE);

        if (mFingerprintManager == null || !mFingerprintManager.isHardwareDetected()) {
            mFingerprintVib.getParent().removePreference(mFingerprintVib);
            mFpKeystore.getParent().removePreference(mFpKeystore);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.XENONHD;
    }
}
