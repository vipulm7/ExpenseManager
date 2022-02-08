package com.VipulMittal.expensemanager.transactionRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepo repo;
    private LiveData<List<Transaction>> transactions;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        repo =new TransactionRepo(application);
    }

    public void Insert(Transaction transaction)
    {
        repo.Insert(transaction);
    }

    public void Delete(Transaction transaction)
    {
        repo.Delete(transaction);
    }

    public void Update(Transaction transaction)
    {
        repo.Update(transaction);
    }

    public LiveData<List<Transaction>> getAllTransactionsMONTH(int month, int year)
    {
        return repo.getAllTransactionsMONTH(month, year);
    }

    public LiveData<List<Transaction>> getAllTransactionsWEEK(int month, int year)
    {
        return repo.getAllTransactionsWEEK(month, year);
    }

    public LiveData<List<Transaction>> getAllTransactionsDAY(int month, int year)
    {
        return repo.getAllTransactionsDAY(month, year);
    }

//    public void UpdateTrans(String note, int amount, String amountDisplay, int accountID, int catID, int subCatID, String description, int type, long date,long dateTime, int id)
//    {
//        repo.UpdateTrans(note, amount, amountDisplay,  accountID, catID, subCatID, description, type, date, dateTime, id);
//    }
}
