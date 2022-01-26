package com.VipulMittal.expensemanager.subCategoryRoom;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.VipulMittal.expensemanager.categoryRoom.Category;

import java.util.Calendar;

@Database(entities = {SubCategory.class, Category.class}, version = 2)
public abstract class SubCategoryDatabase extends RoomDatabase {

    private static SubCategoryDatabase instance;

    public abstract SubCategoryDAO subCategoryDAO();

    public static synchronized SubCategoryDatabase getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    SubCategoryDatabase.class, "subcat_database")
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

            subCategoryDAO.Insert(new SubCategory("By amazon",0,0,1,1));
            subCategoryDAO.Insert(new SubCategory("By gpay",0,0,1,1));
            subCategoryDAO.Insert(new SubCategory("By freecharge",0,0,1,1));

            subCategoryDAO.Insert(new SubCategory("By bank",0,0,2,1));
            subCategoryDAO.Insert(new SubCategory("By uncle",0,0,2,1));



            subCategoryDAO.Insert(new SubCategory("Lunch",0,0,4,2));
            subCategoryDAO.Insert(new SubCategory("BreakFast",0,0,4,2));
            subCategoryDAO.Insert(new SubCategory("Dinner",0,0,4,2));
            subCategoryDAO.Insert(new SubCategory("Restaurant",0,0,4,2));

            subCategoryDAO.Insert(new SubCategory("Bus",0,0,5,2));
            subCategoryDAO.Insert(new SubCategory("Petrol",0,0,5,2));

            subCategoryDAO.Insert(new SubCategory("Medicine",0,0,7,2));
            subCategoryDAO.Insert(new SubCategory("Insurance",0,0,7,2));
            return null;
        }
    }
}
