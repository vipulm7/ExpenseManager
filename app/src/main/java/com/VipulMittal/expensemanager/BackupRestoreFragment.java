package com.VipulMittal.expensemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.room.util.FileUtil;

import android.os.FileUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;

import ir.androidexception.roomdatabasebackupandrestore.Backup;
import ir.androidexception.roomdatabasebackupandrestore.OnWorkFinishListener;

public class BackupRestoreFragment extends PreferenceFragmentCompat {

	MainActivity mainActivity;
	public static final String TAG="Vipul_tag";

	@Override
	public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
		setPreferencesFromResource(R.xml.backup_restore, rootKey);

		Preference backupPreference = findPreference("backup");
		Preference restorePreference = findPreference("restore");
		mainActivity = (MainActivity) getActivity();

		backupPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(@NonNull Preference preference) {

//				File(mainActivity.accountViewModel.repo.accountDatabase.getOpenHelper().getWritableDatabase().getPath())

				int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
				if(permission == PackageManager.PERMISSION_GRANTED)
				{
//
//					File db= mainActivity.getDatabasePath("account_database");
//					File dbShm = new File(db.getParent(), "dbShm");
//					File dbWal = new File(db.getParent(), "dbWal");
//
//					File db2 = new File("/sdcard/", "");
//					File dbShm2 = new File(db2.getParent(), "dbShm");
//					File dbWal2 = new File(db2.getParent(), "dbWal");
//
//					try {
//
//					} catch (Exception e) {
//						Log.e(TAG, "onPreferenceClick: " );
//					}


//					new Backup.Init()
//							.database(mainActivity.accountViewModel.repo.accountDatabase)
//							.path("/sdcard/")
//							.fileName("acc.txt")
//							.onWorkFinishListener(new OnWorkFinishListener() {
//								@Override
//								public void onFinished(boolean success, String message) {
//									if (mainActivity.toast != null)
//										mainActivity.toast.cancel();
//									mainActivity.toast = Toast.makeText(getContext(), "Backup done!", Toast.LENGTH_SHORT);
//									mainActivity.toast.show();
//								}
//							})
//							.execute();
				} else
				{
					if (mainActivity.toast != null)
						mainActivity.toast.cancel();
					mainActivity.toast = Toast.makeText(getContext(), "No write permission!", Toast.LENGTH_SHORT);
					mainActivity.toast.show();
				}

				new Backup.Init()
				.database(mainActivity.accountViewModel.repo.accountDatabase)
				.path("/sdk_gphone_x86_64_arm64/")
				.fileName("acc.txt")
				.onWorkFinishListener((success, message) -> {
					if (mainActivity.toast != null)
						mainActivity.toast.cancel();
					mainActivity.toast = Toast.makeText(getContext(), "Backup done!", Toast.LENGTH_SHORT);
					mainActivity.toast.show();
				})
				.execute();

				return true;
			}
		});

		restorePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(@NonNull Preference preference) {
				return false;
			}
		});
	}
}
