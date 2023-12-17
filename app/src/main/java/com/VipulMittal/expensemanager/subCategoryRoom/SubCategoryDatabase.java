package com.VipulMittal.expensemanager.subCategoryRoom;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.categoryRoom.Category;

@Database(entities = {SubCategory.class, Category.class}, version = 2)
public abstract class SubCategoryDatabase extends RoomDatabase {

	private static SubCategoryDatabase instance;
	private static final Callback roomCallback = new Callback() {
		@Override
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
			super.onCreate(db);
			new PopulateDBAsyncTask(instance).execute();
		}
	};

	public static synchronized SubCategoryDatabase getInstance(Context context) {
		if (instance == null)
			instance = Room.databaseBuilder(context.getApplicationContext(),
							SubCategoryDatabase.class, "subcat_database")
					.fallbackToDestructiveMigration()
					.allowMainThreadQueries()
					.addCallback(roomCallback).build();

		return instance;
	}

	public abstract SubCategoryDAO subCategoryDAO();

	private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
		SubCategoryDAO subCategoryDAO;

		private PopulateDBAsyncTask(SubCategoryDatabase database) {
			subCategoryDAO = database.subCategoryDAO();
		}

		@Override
		protected Void doInBackground(Void... voids) {

			subCategoryDAO.Insert(new SubCategory("By amazon", 0, 0, 2, 1, R.drawable.ia_amazon));
			subCategoryDAO.Insert(new SubCategory("By gpay", 0, 0, 2, 1, R.drawable.ia_google_pay));
			subCategoryDAO.Insert(new SubCategory("By freecharge", 0, 0, 2, 1, R.drawable.ia_freecharge));

			subCategoryDAO.Insert(new SubCategory("By bank", 0, 0, 3, 1, R.drawable.is_bank));
			subCategoryDAO.Insert(new SubCategory("By paytm", 0, 0, 3, 1, R.drawable.ia_paytm));

			subCategoryDAO.Insert(new SubCategory("Fast Food", 0, 0, 4, 2, R.drawable.is_hamburger));
			subCategoryDAO.Insert(new SubCategory("Fruits", 0, 0, 4, 2, R.drawable.is_orange));
			subCategoryDAO.Insert(new SubCategory("Vegetables", 0, 0, 4, 2, R.drawable.is_potato));
			subCategoryDAO.Insert(new SubCategory("Restaurant", 0, 0, 4, 2, R.drawable.is_cafe));

			subCategoryDAO.Insert(new SubCategory("Bus", 0, 0, 5, 2, R.drawable.is_bus));
			subCategoryDAO.Insert(new SubCategory("Petrol", 0, 0, 5, 2, R.drawable.is_gas_station));
			subCategoryDAO.Insert(new SubCategory("Taxi", 0, 0, 5, 2, R.drawable.is_taxi));

			subCategoryDAO.Insert(new SubCategory("Medicine", MainActivity.sum_amounts, 0, 7, 2, R.drawable.is_pills));
			subCategoryDAO.Insert(new SubCategory("Insurance", 0, 0, 7, 2, R.drawable.is_treatment_list));

			subCategoryDAO.Insert(new SubCategory("Movie", 0, 0, 8, 2, R.drawable.is_ticket));
			subCategoryDAO.Insert(new SubCategory("Netflix", 0, 0, 8, 2, R.drawable.is_netflix));

			return null;
		}
	}
}
