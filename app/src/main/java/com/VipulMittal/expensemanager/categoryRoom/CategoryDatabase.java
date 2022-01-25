package com.VipulMittal.expensemanager.categoryRoom;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;

import java.util.Calendar;

@Database(entities = {SubCategory.class, Category.class}, version = 2)
public abstract class CategoryDatabase extends RoomDatabase {

    private static CategoryDatabase instance;

    public abstract CategoryDAO categoryDAO();

    public static synchronized CategoryDatabase getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    CategoryDatabase.class, "category_database")
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
            categoryDAO =database.categoryDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            categoryDAO.Insert(new Category("Salary",0,0,0,1));
            categoryDAO.Insert(new Category("Cashback",0,0,3,1));
            categoryDAO.Insert(new Category("Interest",0,0,2,1));

            categoryDAO.Insert(new Category("Food",0,0,4,2));
            categoryDAO.Insert(new Category("Transport",0,0,2,2));
            categoryDAO.Insert(new Category("Beauty",0,0,0,2));
            categoryDAO.Insert(new Category("Health",0,0,2,2));
            return null;
        }
    }
}
