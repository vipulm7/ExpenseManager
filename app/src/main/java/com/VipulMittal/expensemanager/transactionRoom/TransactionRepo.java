package com.VipulMittal.expensemanager.transactionRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TransactionRepo
{
    private TransactionDAO transactionDAO;
    private LiveData<List<Transaction>> transactions;

    public TransactionRepo(Application application)
    {
        TransactionDatabase transactionDatabase = TransactionDatabase.getInstance(application);
        transactionDAO= transactionDatabase.transactionDAO();
    }

    public void Insert (Transaction transaction)
    {
        new InsertNoteASyncTask(transactionDAO).execute(transaction);
    }

    public void Delete(Transaction transaction)
    {
        new DeleteNoteASyncTask(transactionDAO).execute(transaction);
    }

    public void Update(Transaction transaction)
    {
        new UpdateNoteASyncTask(transactionDAO).execute(transaction);
    }

    public LiveData<List<Transaction>> getAllTransactions(int month, int year)
    {
        transactions=transactionDAO.getAllTransactions(month, year);
        return transactions;
    }

    public void UpdateTrans(String note, int amount, String amountDisplay, int accountID, int catID, int subCatID, String description, int type, long date,long dateTime, int id)
    {
        transactionDAO.UpdateTrans(note, amount, amountDisplay,  accountID, catID, subCatID, description, type, date, dateTime, id);
    }


    private static class InsertNoteASyncTask extends AsyncTask<Transaction,Void,Void>
    {
        private TransactionDAO transactionDAO;

        private InsertNoteASyncTask(TransactionDAO transactionDAO)
        {
            this.transactionDAO=transactionDAO;
        }
        @Override
        protected Void doInBackground(Transaction... transactions) {
            this.transactionDAO.Insert(transactions[0]);
            return null;
        }
    }

    private static class DeleteNoteASyncTask extends AsyncTask<Transaction,Void,Void>
    {
        private TransactionDAO transactionDAO;

        private DeleteNoteASyncTask(TransactionDAO transactionDAO)
        {
            this.transactionDAO=transactionDAO;
        }
        @Override
        protected Void doInBackground(Transaction... transactions) {
            this.transactionDAO.Delete(transactions[0]);
            return null;
        }
    }

    private static class UpdateNoteASyncTask extends AsyncTask<Transaction,Void,Void>
    {
        private TransactionDAO transactionDAO;

        private UpdateNoteASyncTask(TransactionDAO transactionDAO)
        {
            this.transactionDAO=transactionDAO;
        }
        @Override
        protected Void doInBackground(Transaction... transactions) {
            this.transactionDAO.Update(transactions[0]);
            return null;
        }
    }
}
