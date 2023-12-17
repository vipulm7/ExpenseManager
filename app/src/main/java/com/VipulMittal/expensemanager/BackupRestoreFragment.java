package com.VipulMittal.expensemanager;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class BackupRestoreFragment extends PreferenceFragmentCompat {

	public static final String TAG = "Vipul_tag";
	MainActivity mainActivity;

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

				if (true) {
					File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Expense Tracker");

					if (!folder.exists()) {
						if (!folder.mkdirs()) {
							if (mainActivity.toast != null)
								mainActivity.toast.cancel();
							mainActivity.toast = Toast.makeText(getContext(), "Can't create directory!", Toast.LENGTH_SHORT);
							mainActivity.toast.show();
						}
					}

					File sd = Environment.getExternalStorageDirectory();
					File data = Environment.getDataDirectory();

					if (sd.canWrite()) {
						String currentDBPath = "//data//ExpenseTracker//databases//trans_database";
						String backupDBPath = "/BackupFolder/trans_database";
						File currentDB = new File(data, currentDBPath);
						File backupDB = new File(sd, backupDBPath);

						try {
							FileChannel src = new FileInputStream(currentDB).getChannel();
							FileChannel dst = new FileInputStream(backupDB).getChannel();

							dst.transferFrom(src, 0, src.size());
							src.close();
							dst.close();

							if (mainActivity.toast != null)
								mainActivity.toast.cancel();
							mainActivity.toast = Toast.makeText(getContext(), "Backup created successfully!", Toast.LENGTH_SHORT);
							mainActivity.toast.show();

						} catch (FileNotFoundException e) {
							e.printStackTrace();
							mainActivity.toast = Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG);
							mainActivity.toast.show();
						} catch (IOException e) {
							e.printStackTrace();
							mainActivity.toast = Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG);
							mainActivity.toast.show();
						}
					} else {
						mainActivity.toast = Toast.makeText(getContext(), "Can't write!", Toast.LENGTH_LONG);
						mainActivity.toast.show();
					}

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
				} else {
					if (mainActivity.toast != null)
						mainActivity.toast.cancel();
					mainActivity.toast = Toast.makeText(getContext(), "No write permission!", Toast.LENGTH_SHORT);
					mainActivity.toast.show();
				}

//				new Backup.Init()
//				.database(mainActivity.accountViewModel.repo.accountDatabase)
//				.path("/sdk_gphone_x86_64_arm64/")
//				.fileName("acc.txt")
//				.onWorkFinishListener((success, message) -> {
//					if (mainActivity.toast != null)
//						mainActivity.toast.cancel();
//					mainActivity.toast = Toast.makeText(getContext(), "Backup done!", Toast.LENGTH_SHORT);
//					mainActivity.toast.show();
//				})
//				.execute();

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
