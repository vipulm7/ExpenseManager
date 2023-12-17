package com.VipulMittal.expensemanager.transactionRoom;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.VipulMittal.expensemanager.MainActivity;

import java.util.Calendar;

@Database(entities = Transaction.class, version = 1)
public abstract class TransactionDatabase extends RoomDatabase {

	private static TransactionDatabase instance;
	private static final Callback roomCallback = new Callback() {
		@Override
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
			super.onCreate(db);
			new PopulateDBAsyncTask(instance).execute();
		}
	};

	public static synchronized TransactionDatabase getInstance(Context context) {
		if (instance == null)
			instance = Room.databaseBuilder(context.getApplicationContext(),
							TransactionDatabase.class, "trans_database")
					.fallbackToDestructiveMigration()
					.allowMainThreadQueries()
					.addCallback(roomCallback).build();

		return instance;
	}

	public static long getDate(Calendar calendar) {
		long a = calendar.getTimeInMillis() - calendar.get(Calendar.SECOND) * 1000 - calendar.get(Calendar.MINUTE) * 60000 - calendar.get(Calendar.MILLISECOND) - calendar.get(Calendar.HOUR_OF_DAY) * 3600000;
		return a;
	}

	public abstract TransactionDAO transactionDAO();

	private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
		TransactionDAO transactionDAO;

		private PopulateDBAsyncTask(TransactionDatabase database) {
			transactionDAO = database.transactionDAO();
		}

		@Override
		protected Void doInBackground(Void... voids) {

//            transactionDAO.Insert(new Transaction("Sample1", -100, 1,4,8,"Des",2, getDate(Calendar.getInstance()),Calendar.getInstance().getTimeInMillis()));
//            transactionDAO.Insert(new Transaction("Sample2", -302, 2,7,12,"Des",2, getDate(Calendar.getInstance()),Calendar.getInstance().getTimeInMillis()-86400000*2L));
//            transactionDAO.Insert(new Transaction("Sample3", -32, 3,7,12,"Des",2, getDateBefore(Calendar.getInstance()),Calendar.getInstance().getTimeInMillis()-86400000));
//            transactionDAO.Insert(new Transaction("Sample4", -37,2,7,12,"Des",2, getDate(Calendar.getInstance()),Calendar.getInstance().getTimeInMillis()-86400000*2L));
//            transactionDAO.Insert(new Transaction("Sample5", -500,2,7,12,"Des",2, getDate(Calendar.getInstance()),Calendar.getInstance().getTimeInMillis()-86400000*3L));
//            transactionDAO.Insert(new Transaction("Sample4", -37,2,7,12,"Des",2, getDate(Calendar.getInstance()),Calendar.getInstance().getTimeInMillis()-86400000*4L));
//            transactionDAO.Insert(new Transaction("Sample4", -37,2,7,12,"Des",2, getDate(Calendar.getInstance()),Calendar.getInstance().getTimeInMillis()-86400000*5L));

			for (int i = -1; ++i < 10; ) {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -2 * i);
				transactionDAO.Insert(new Transaction("Sample" + i, MainActivity.amount[i], 2, 7, 13, "Des", 2, getDate(calendar), Calendar.getInstance().getTimeInMillis() - 86400000 * 2L * i));
			}


//            for(int i=-1;++i<30;) {
//                Calendar calendar=Calendar.getInstance();
//                calendar.add(Calendar.DATE, 2*i+1);
//                transactionDAO.Insert(new Transaction("Sample" + i, -(int) (Math.random() * 1000), 2, 7, 12, "Des", 2, getDate(calendar), Calendar.getInstance().getTimeInMillis() + 86400000L + 86400000 * 2L * i));
//            }

			return null;
		}
	}

}
