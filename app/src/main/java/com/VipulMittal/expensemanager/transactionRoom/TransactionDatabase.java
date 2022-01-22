package com.VipulMittal.expensemanager.transactionRoom;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;

@Database(entities = Transaction.class, version = 2)
public abstract class TransactionDatabase extends RoomDatabase {

    private static TransactionDatabase instance;

    public abstract TransactionDAO transactionDAO();

    public static synchronized TransactionDatabase getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    TransactionDatabase.class, "trans_database")
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
        TransactionDAO transactionDAO;

        private PopulateDBAsyncTask(TransactionDatabase database)
        {
            transactionDAO=database.transactionDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

//            transactionDAO.Insert(new Transaction("Grofers",1032));
            transactionDAO.Insert(new Transaction("Sample",100, "100", "Cash", "Food", "Grofers",
                    "Desciption", getDate(Calendar.getInstance()), 2));
            return null;
        }
    }

    public static long getDate(Calendar calendar)
    {
        long a=calendar.getTimeInMillis()-calendar.get(Calendar.SECOND)*1000-calendar.get(Calendar.MINUTE)*60000-calendar.get(Calendar.MILLISECOND)-calendar.get(Calendar.HOUR_OF_DAY)*3600000;
        return a/1000L;
    }
}
