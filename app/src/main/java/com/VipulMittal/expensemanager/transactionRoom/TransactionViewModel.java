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

    public LiveData<List<Transaction>> getAllTransactions(int month, int year)
    {
        transactions= repo.getAllTransactions(month, year);
        return transactions;
    }

    public void UpdateTrans(String note, int amount, String amountDisplay, int accountID, int catID, int subCatID, String description, int type, long date,long dateTime, int id)
    {
        repo.UpdateTrans(note, amount, amountDisplay,  accountID, catID, subCatID, description, type, date, dateTime, id);
    }
}
