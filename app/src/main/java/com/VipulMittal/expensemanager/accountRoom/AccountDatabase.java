package com.VipulMittal.expensemanager.accountRoom;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;

@Database(entities = Account.class, version = 2)
public abstract class AccountDatabase extends RoomDatabase {

    private static AccountDatabase instance;

    public abstract AccountDAO accountDAO();

    public static synchronized AccountDatabase getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    AccountDatabase.class, "account_database")
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
        AccountDAO accountDAO;

        private PopulateDBAsyncTask(AccountDatabase database)
        {
            accountDAO =database.accountDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            accountDAO.Insert(new Account("Cash"));
            accountDAO.Insert(new Account("Card"));
            accountDAO.Insert(new Account("E-Wallet"));
            return null;
        }
    }
}
