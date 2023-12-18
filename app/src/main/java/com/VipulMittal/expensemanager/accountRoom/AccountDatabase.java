package com.VipulMittal.expensemanager.accountRoom;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = Account.class, version = 2)
public abstract class AccountDatabase extends RoomDatabase {

	public static AccountDatabase instance;
	private static final Callback roomCallback = new Callback() {
		@Override
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
			super.onCreate(db);
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			AccountDAO accountDAO = instance.accountDAO();
			executorService.execute(() -> {
				accountDAO.Insert(new Account("Cash", 0, 0, R.drawable.ia_cash));
				accountDAO.Insert(new Account("Debit Card", MainActivity.sum_amounts, 0, R.drawable.ia_visa));
				accountDAO.Insert(new Account("Paytm", 0, 0, R.drawable.ia_paytm));
			});
		}
	};

	public static synchronized AccountDatabase getInstance(Context context) {
		if (instance == null)
			instance = Room.databaseBuilder(context.getApplicationContext(),
							AccountDatabase.class, "account_database")
					.fallbackToDestructiveMigration()
					.allowMainThreadQueries()
					.setJournalMode(JournalMode.TRUNCATE)
					.addCallback(roomCallback).build();

		return instance;
	}

	public abstract AccountDAO accountDAO();
}
