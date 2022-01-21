package com.VipulMittal.expensemanager.dateRoom;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;

@Database(entities = Date.class, version = 1)
public abstract class DateDatabase extends RoomDatabase {

	private static DateDatabase instance;

	public abstract DateDAO dateDAO();

	public static synchronized DateDatabase getInstance(Context context)
	{
		if(instance==null)
			instance= Room.databaseBuilder(context.getApplicationContext(),DateDatabase.class,
					  "date_database")
					  .fallbackToDestructiveMigration().addCallback(roomCallback).build();

		return instance;
	}

	private static RoomDatabase.Callback roomCallback= new RoomDatabase.Callback()
	{
		@Override
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
			super.onCreate(db);
			new PopulateDBAsyncTask(instance).execute();
		}
	};

	private static class PopulateDBAsyncTask extends AsyncTask<Void , Void, Void>
	{
		DateDAO dateDAO;
		private PopulateDBAsyncTask(DateDatabase database)
		{
			dateDAO=database.dateDAO();
		}

		@Override
		protected Void doInBackground(Void... voids) {
			dateDAO.Insert(new Date(Calendar.getInstance().getTimeInMillis()));
			return null;
		}
	}
}
