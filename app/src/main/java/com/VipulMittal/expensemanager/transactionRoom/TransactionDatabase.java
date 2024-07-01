package com.VipulMittal.expensemanager.transactionRoom;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.VipulMittal.expensemanager.MainActivity;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = Transaction.class, version = 1)
public abstract class TransactionDatabase extends RoomDatabase {

	public static TransactionDatabase instance;
	private static final Callback roomCallback = new Callback() {
		@Override
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
			super.onCreate(db);
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			TransactionDAO transactionDAO = instance.transactionDAO();
			executorService.execute(() -> {
//				transactionDAO.Insert(new Transaction("Sample1", -100, 1, 4, 8, "Des", 2, getDate(Calendar.getInstance()), Calendar.getInstance().getTimeInMillis()));
//				transactionDAO.Insert(new Transaction("Sample2", -302, 2, 7, 12, "Des", 2, getDate(Calendar.getInstance()), Calendar.getInstance().getTimeInMillis() - 86400000 * 2L));
//				transactionDAO.Insert(new Transaction("Sample3", -32, 3, 7, 12, "Des", 2, getDateBefore(Calendar.getInstance()), Calendar.getInstance().getTimeInMillis() - 86400000));
//				transactionDAO.Insert(new Transaction("Sample4", -37, 2, 7, 12, "Des", 2, getDate(Calendar.getInstance()), Calendar.getInstance().getTimeInMillis() - 86400000 * 2L));
//				transactionDAO.Insert(new Transaction("Sample5", -500, 2, 7, 12, "Des", 2, getDate(Calendar.getInstance()), Calendar.getInstance().getTimeInMillis() - 86400000 * 3L));
//				transactionDAO.Insert(new Transaction("Sample4", -37, 2, 7, 12, "Des", 2, getDate(Calendar.getInstance()), Calendar.getInstance().getTimeInMillis() - 86400000 * 4L));
//				transactionDAO.Insert(new Transaction("Sample4", -37,2,7,12,"Des",2, getDate(Calendar.getInstance()),Calendar.getInstance().getTimeInMillis()-86400000*5L));

				int size = 10;
				Transaction[] transactions = new Transaction[size];
				for (int i = -1; ++i < size; ) {
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DATE, -1 * i);
					transactions[i] = new Transaction("Sample" + i, MainActivity.amount[i], 2, 7, 13, "Des", 2, getDate(calendar), Calendar.getInstance().getTimeInMillis() - 86400000 * 2L * i);
				}
				transactionDAO.InsertTransactions(transactions);
			});
		}
	};

	public static synchronized TransactionDatabase getInstance(Context context) {
		if (instance == null)
			instance = Room.databaseBuilder(context.getApplicationContext(),
							TransactionDatabase.class, "trans_database")
					.fallbackToDestructiveMigration()
					.allowMainThreadQueries()
					.setJournalMode(JournalMode.TRUNCATE)
					.addCallback(roomCallback).build();

		return instance;
	}

	public static long getDate(Calendar calendar) {
		return calendar.getTimeInMillis() - calendar.get(Calendar.SECOND) * 1000 - calendar.get(Calendar.MINUTE) * 60000 - calendar.get(Calendar.MILLISECOND) - calendar.get(Calendar.HOUR_OF_DAY) * 3600000;
	}

	public abstract TransactionDAO transactionDAO();

}
