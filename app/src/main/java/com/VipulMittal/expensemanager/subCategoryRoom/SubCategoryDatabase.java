package com.VipulMittal.expensemanager.subCategoryRoom;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;

@Database(entities = SubCategory.class, version = 1)
public abstract class SubCategoryDatabase extends RoomDatabase {

    private static SubCategoryDatabase instance;

    public abstract SubCategoryDAO subCategoryDAO();

    public static synchronized SubCategoryDatabase getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    SubCategoryDatabase.class, "subcategory_database")
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
        SubCategoryDAO subCategoryDAO;

        private PopulateDBAsyncTask(SubCategoryDatabase database)
        {
            subCategoryDAO =database.subCategoryDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            subCategoryDAO.Insert(new SubCategory("Lunch",0,0,1,0));
            subCategoryDAO.Insert(new SubCategory("Dinner",0,0,1,0));
            subCategoryDAO.Insert(new SubCategory("Grocery",0,0,1,0));
            return null;
        }
    }

    public static long getDate(Calendar calendar)
    {
        long a=calendar.getTimeInMillis()-calendar.get(Calendar.SECOND)*1000-calendar.get(Calendar.MINUTE)*60000-calendar.get(Calendar.MILLISECOND)-calendar.get(Calendar.HOUR_OF_DAY)*3600000;
        return a/1000L;
    }
}
