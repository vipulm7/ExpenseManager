package com.VipulMittal.expensemanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.VipulMittal.expensemanager.accountRoom.AccountDatabase;
import com.VipulMittal.expensemanager.categoryRoom.CategoryDatabase;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryDatabase;
import com.VipulMittal.expensemanager.transactionRoom.TransactionDatabase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class BackupRestoreFragment extends PreferenceFragmentCompat {

	public static final String TAG = "Vipul_tag";
	MainActivity mainActivity;
	ActivityResultLauncher<Intent> exportDBLauncher, importDBLauncher;

	@Override
	public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
		setPreferencesFromResource(R.xml.backup_restore, rootKey);

		Preference backupPreference = findPreference("backup");
		Preference restorePreference = findPreference("restore");
		mainActivity = (MainActivity) requireActivity();
		mainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

		assert backupPreference != null;
		assert restorePreference != null;

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O)
			return;

		backupPreference.setOnPreferenceClickListener(preference -> {
			try {
				Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("application/zip");
				intent.putExtra(Intent.EXTRA_TITLE, "ExpenseManager_db" + ".zip");
				exportDBLauncher.launch(intent);
			} catch (Exception e) {
				Log.d(TAG, "exportDatabase: error = " + e);
				Toast.makeText(requireContext(), "Backup not created", Toast.LENGTH_LONG).show();
			}

			return true;
		});

		restorePreference.setOnPreferenceClickListener(preference -> {
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("application/zip");
			importDBLauncher.launch(intent);

			return true;
		});

		exportDBLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
			if (result.getResultCode() == Activity.RESULT_OK) {
				Intent intent = result.getData();
				if (intent != null) {
					Uri uri = intent.getData();
					if (uri != null) {
						backupDatabase(uri);
						Toast.makeText(requireContext(), "Backup created", Toast.LENGTH_LONG).show();
					} else
						Toast.makeText(requireContext(), "Backup not created", Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(requireContext(), "Backup not created", Toast.LENGTH_LONG).show();
			} else
				Toast.makeText(requireContext(), "Backup not created", Toast.LENGTH_LONG).show();
		});

		importDBLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
			if (result.getResultCode() == Activity.RESULT_OK) {
				Intent intent = result.getData();
				if (intent != null) {
					Uri uri = intent.getData();
					restoreDatabases(uri);
				}
			}
		});

	}

	private void addDatabaseToZip(String databaseName, ZipOutputStream zipOutputStream) {
		try {
			String databasePath = requireActivity().getDatabasePath(databaseName).getAbsolutePath();
			InputStream inputStream = Files.newInputStream(Paths.get(databasePath));
			ZipEntry zipEntry = new ZipEntry(databaseName + ".db");
			zipOutputStream.putNextEntry(zipEntry);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0)
				zipOutputStream.write(buffer, 0, length);

			zipOutputStream.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void backupDatabase(Uri uri) {

		try {
			OutputStream outputStream = requireActivity().getContentResolver().openOutputStream(uri);
			ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

			addDatabaseToZip("category_database", zipOutputStream);
			addDatabaseToZip("subcat_database", zipOutputStream);
			addDatabaseToZip("account_database", zipOutputStream);
			addDatabaseToZip("trans_database", zipOutputStream);

			zipOutputStream.flush();
			zipOutputStream.finish();
			zipOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void restoreDatabase(ZipInputStream zipInputStream, String entryName) {
		try {
			File dbFile = requireActivity().getDatabasePath(entryName);
			OutputStream outputStream = Files.newOutputStream(dbFile.toPath());
			byte[] buffer = new byte[1024];
			int length;

			while ((length = zipInputStream.read(buffer)) > 0)
				outputStream.write(buffer, 0, length);

			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void restoreDatabases(Uri uri) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(() -> {
			Log.d(TAG, "restoreDatabase: start");
			CategoryDatabase.instance.clearAllTables();
			SubCategoryDatabase.instance.clearAllTables();
			AccountDatabase.instance.clearAllTables();
			TransactionDatabase.instance.clearAllTables();
			Log.d(TAG, "restoreDatabase: tables cleared");

			mainActivity.runOnUiThread(() -> {
				try {
					InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
					ZipInputStream zipInputStream = new ZipInputStream(inputStream);
					ZipEntry zipEntry;

					// Loop through each entry in the ZIP file
					while ((zipEntry = zipInputStream.getNextEntry()) != null) {
						String entryName = zipEntry.getName();
						entryName = entryName.substring(0, entryName.length() - 3);
						Log.d(TAG, "restoreDatabases: entryName=" + entryName);
						restoreDatabase(zipInputStream, entryName);
						zipInputStream.closeEntry();
					}
					zipInputStream.close();

					mainActivity.transactionROOM();
					mainActivity.accountROOM();
					mainActivity.categoryROOM();
					mainActivity.subCatROOM();
					mainActivity.subCategoryROOM(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		});
	}

	private String timeStamp() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR) + "_" + (calendar.get(Calendar.MONTH) + 1) + "_" +
				calendar.get(Calendar.DATE) + "_" + calendar.get(Calendar.HOUR_OF_DAY) +
				"_" + calendar.get(Calendar.MINUTE) + "_" + calendar.get(Calendar.SECOND);
	}
}
