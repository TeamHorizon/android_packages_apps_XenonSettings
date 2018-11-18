/*
 * Copyright (C) 2018 Android Ice Cold Project
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

package com.xenonhd.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.SearchIndexableResource;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;

import com.xenonhd.settings.XenonSettings;
import com.xenonhd.settings.R;
import com.xenonhd.settings.preferences.Utils;

import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ThemeSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.xenonhd_themes);

        findPreference(Settings.System.THEMING_BASE).setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (Settings.System.THEMING_BASE.equals(preference.getKey())) {
            // If notifications are themed (both previously or as a result),
            // we need to restart SystemUI for changes to have effect
            if (hasDarkNotifications(Integer.parseInt((String) newValue)) ||
                    hasDarkNotifications(Settings.System.getInt(getActivity().getContentResolver(),
                            Settings.System.THEMING_BASE, 0))) {
                // Keep a context even if activity gets closed
                final Context appContext = getActivity().getApplicationContext();
                mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Utils.restartSystemUi(appContext);
                        }
                });
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean hasDarkNotifications(int baseThemePref) {
            return baseThemePref >= 3 && baseThemePref <= 6;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.XENONHD_SETTINGS;
    }

    /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                 @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                     final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.xenonhd_themes;
                    result.add(sir);
                    return result;
                }
                 @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };
}
