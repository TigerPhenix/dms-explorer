/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;

import net.mm2d.android.activity.AppCompatPreferenceActivity;
import net.mm2d.android.util.LaunchUtils;
import net.mm2d.dmsexplorer.BuildConfig;
import net.mm2d.dmsexplorer.R;
import net.mm2d.dmsexplorer.Repository;
import net.mm2d.dmsexplorer.domain.model.OpenUriCustomTabsModel;
import net.mm2d.dmsexplorer.domain.model.OpenUriModel;
import net.mm2d.dmsexplorer.settings.Key;
import net.mm2d.dmsexplorer.view.dialog.WebViewDialog;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

import java.util.List;

/**
 * アプリ設定を行うActivity。
 *
 * @author <a href="mailto:ryo@mm2d.net">大前良介(OHMAE Ryosuke)</a>
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * このActivityを起動するためのIntentを作成する。
     *
     * <p>Extraの設定と読み出しをこのクラス内で完結させる。
     * 現時点ではExtraは設定していない。
     *
     * @param context コンテキスト
     * @return このActivityを起動するためのIntent
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Repository.get().getThemeModel().setThemeColor(this,
                ContextCompat.getColor(this, R.color.primary),
                ContextCompat.getColor(this, R.color.defaultStatusBar));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || PlaybackPreferenceFragment.class.getName().equals(fragmentName)
                || FunctionPreferenceFragment.class.getName().equals(fragmentName)
                || InformationPreferenceFragment.class.getName().equals(fragmentName);
    }

    private static boolean canUseChromeCustomTabs(Context context) {
        return !TextUtils.isEmpty(CustomTabsHelper.getPackageNameToUse(context));
    }

    public static class PlaybackPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_playback);
        }
    }

    public static class FunctionPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_function);
            setUpCustomTabs();
        }

        private void setUpCustomTabs() {
            final SwitchPreference customTabs = (SwitchPreference) findPreference(Key.USE_CUSTOM_TABS.name());
            customTabs.setOnPreferenceChangeListener((preference, newValue) -> {
                final OpenUriModel model = Repository.get().getOpenUriModel();
                if ((newValue instanceof Boolean) && (model instanceof OpenUriCustomTabsModel)) {
                    ((OpenUriCustomTabsModel) model).setUseCustomTabs((Boolean) newValue);
                }
                return true;
            });
            if (canUseChromeCustomTabs(getActivity())) {
                return;
            }
            if (customTabs.isChecked()) {
                customTabs.setChecked(false);
            }
            customTabs.setEnabled(false);
        }
    }

    public static class InformationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_information);
            findPreference(Key.PLAY_STORE.name()).setOnPreferenceClickListener(preference -> {
                final Context context = preference.getContext();
                LaunchUtils.openUri(context, "market://details?id=" + context.getPackageName());
                return true;
            });
            findPreference(Key.VERSION_NUMBER.name()).setSummary(BuildConfig.VERSION_NAME);
            findPreference(Key.LICENSE.name()).setOnPreferenceClickListener(preference -> {
                final WebViewDialog dialog = WebViewDialog.newInstance(
                        getString(R.string.pref_title_license),
                        "file:///android_asset/license.html");
                dialog.show(getFragmentManager(), "");
                return true;
            });
        }
    }
}
