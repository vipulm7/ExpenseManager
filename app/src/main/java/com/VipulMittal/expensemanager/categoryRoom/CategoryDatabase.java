package com.VipulMittal.expensemanager.categoryRoom;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;

@Database(entities = Category.class, version = 1)
public abstract class CategoryDatabase extends RoomDatabase {

    private static CategoryDatabase instance;

    public abstract CategoryDAO transactionDAO();

    public static synchronized CategoryDatabase getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    CategoryDatabase.class, "trans_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback).build();

        return instance;
    }

    private static Callback roomCallback=new Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void>
    {
        CategoryDAO categoryDAO;

        private PopulateDBAsyncTask(CategoryDatabase database)
        {
            categoryDAO =database.transactionDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            categoryDAO.Insert(new Category("Food",0,0,1));
            return null;
        }
    }

    public static long getDate(Calendar calendar)
    {
        long a=calendar.getTimeInMillis()-calendar.get(Calendar.SECOND)*1000-calendar.get(Calendar.MINUTE)*60000-calendar.get(Calendar.MILLISECOND)-calendar.get(Calendar.HOUR_OF_DAY)*3600000;
        return a/1000L;
    }
}
