package com.VipulMittal.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class SettingsFragment extends PreferenceFragmentCompat {

	public static final String TAG="Vipul_tag";
	ActivityResultLauncher<Intent> abc;
	BiometricManager biometricManager;
	MainActivity mainActivity;
	Preference.OnPreferenceChangeListener fps_listener;

	@Override
	public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
		setPreferencesFromResource(R.xml.main, rootKey);

		EditTextPreference editTextPreference= findPreference("password");
		SwitchPreference passwordSwitch = findPreference("passwordOnOff");
		SwitchPreference fingerprintSwitch = findPreference("fingerprint");
		editTextPreference.setVisible(passwordSwitch.isChecked());
		fingerprintSwitch.setVisible(passwordSwitch.isChecked());
		mainActivity = (MainActivity) getActivity();

		abc = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
			@Override
			public void onActivityResult(ActivityResult result) {
				biometricManager = BiometricManager.from(getContext());
				switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
//					case BiometricManager.BIOMETRIC_SUCCESS:
//						Log.d(TAG, "Biometric: success");
//						fingerprintSwitch.setOnPreferenceChangeListener(null);
//						fingerprintSwitch.setChecked(true);
//						fingerprintSwitch.setOnPreferenceChangeListener(listener);
//						break;
					case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
//							Intent addFingerIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
//							addFingerIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG);
//
//							abc.launch(addFingerIntent);
//						}
//						fingerprintSwitch.setOnPreferenceChangeListener(null);
//						fingerprintSwitch.setChecked(false);
//						fingerprintSwitch.setOnPreferenceChangeListener(fps_listener);
						Log.d(TAG, "Biometric: None enrolled");
						break;
					case BiometricManager.BIOMETRIC_SUCCESS:
						fingerprintSwitch.setChecked(true);
						break;
				}
			}
		});

		passwordSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
				boolean on=(boolean) newValue;
				editTextPreference.setVisible(on);
				fingerprintSwitch.setVisible(on);

				if(on) {
					biometricManager = BiometricManager.from(getContext());
					fingerprintSwitch.setEnabled(true);
					switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
						case BiometricManager.BIOMETRIC_SUCCESS:
							Log.d(TAG, "Biometric: success");
							break;
						case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
							Log.d(TAG, "Biometric: No Hardware");
							fingerprintSwitch.setEnabled(false);
							break;
						case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
							Log.d(TAG, "Biometric: None enrolled");
							break;
					}
				}

				Log.d(TAG, "onPreferenceChange:  on = "+on);
				return true;
			}
		});

		fps_listener = new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
				boolean checked=(boolean) newValue;

				fingerprintSwitch.setOnPreferenceChangeListener(null);
				fingerprintSwitch.setChecked(false);
				fingerprintSwitch.setOnPreferenceChangeListener(fps_listener);

				if(checked)
				{
					biometricManager = BiometricManager.from(getContext());
					if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
							Intent addFingerIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
							addFingerIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG);

							abc.launch(addFingerIntent);
						}
					}
					else if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
						mainActivity.executor = ContextCompat.getMainExecutor(getContext());
						mainActivity.biometricPrompt = new BiometricPrompt(getActivity(), mainActivity.executor, new BiometricPrompt.AuthenticationCallback() {
							@Override
							public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
								super.onAuthenticationError(errorCode, errString);
								if (mainActivity.toast != null)
									mainActivity.toast.cancel();
								mainActivity.toast = Toast.makeText(getContext(), "Try again!", Toast.LENGTH_SHORT);
								mainActivity.toast.show();
							}

							@Override
							public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
								super.onAuthenticationSucceeded(result);
								if (mainActivity.toast != null)
									mainActivity.toast.cancel();
								mainActivity.toast = Toast.makeText(getContext(), "Fingerprint set!", Toast.LENGTH_SHORT);
								mainActivity.toast.show();
							}

							@Override
							public void onAuthenticationFailed() {
								super.onAuthenticationFailed();
								if (mainActivity.toast != null)
									mainActivity.toast.cancel();
								mainActivity.toast = Toast.makeText(getContext(), "Not user!", Toast.LENGTH_SHORT);
								mainActivity.toast.show();
							}
						});


						mainActivity.promptInfo = new BiometricPrompt.PromptInfo.Builder()
								.setTitle("Confirm fingerprint")
								.setSubtitle("Touch fingerprint sensor!")
								.setNegativeButtonText("Cancel")
								.build();

						mainActivity.biometricPrompt.authenticate(mainActivity.promptInfo);
					}
				}

				return true;
			}
		};

		fingerprintSwitch.setOnPreferenceChangeListener(fps_listener);
	}
}
