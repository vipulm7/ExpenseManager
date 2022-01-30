package com.VipulMittal.expensemanager.accountRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountRepo
{
    private AccountDAO accountDAO;
    private LiveData<List<Account>> accounts;

    public AccountRepo(Application application)
    {
        AccountDatabase accountDatabase = AccountDatabase.getInstance(application);
        accountDAO = accountDatabase.accountDAO();
    }

    public void Insert (Account account)
    {
        new InsertNoteASyncTask(accountDAO).execute(account);
    }

    public void Delete(Account account)
    {
        new DeleteNoteASyncTask(accountDAO).execute(account);
    }

    public void Update(Account account)
    {
        new UpdateNoteASyncTask(accountDAO).execute(account);
    }

    public LiveData<List<Account>> getAllAccounts()
    {
        accounts = accountDAO.getAllAccounts();
        return accounts;
    }

    public Account getAcc (int aID)
    {
        return accountDAO.getAcc(aID);
    }


    private static class InsertNoteASyncTask extends AsyncTask<Account,Void,Void>
    {
        private AccountDAO accountDAO;

        private InsertNoteASyncTask(AccountDAO accountDAO)
        {
            this.accountDAO = accountDAO;
        }
        @Override
        protected Void doInBackground(Account... accounts) {
            this.accountDAO.Insert(accounts[0]);
            return null;
        }
    }

    private static class DeleteNoteASyncTask extends AsyncTask<Account,Void,Void>
    {
        private AccountDAO accountDAO;

        private DeleteNoteASyncTask(AccountDAO accountDAO)
        {
            this.accountDAO = accountDAO;
        }
        @Override
        protected Void doInBackground(Account... accounts) {
            this.accountDAO.Delete(accounts[0]);
            return null;
        }
    }

    private static class UpdateNoteASyncTask extends AsyncTask<Account,Void,Void>
    {
        private AccountDAO accountDAO;

        private UpdateNoteASyncTask(AccountDAO accountDAO)
        {
            this.accountDAO = accountDAO;
        }
        @Override
        protected Void doInBackground(Account... accounts) {
            this.accountDAO.Update(accounts[0]);
            return null;
        }
    }
}
