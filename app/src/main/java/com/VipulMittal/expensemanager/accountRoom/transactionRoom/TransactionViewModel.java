package com.VipulMittal.expensemanager.accountRoom.transactionRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionRepo;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepo repo;
    private LiveData<List<Transaction>> transactions;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        repo =new TransactionRepo(application);
        transactions= repo.getAllData();
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

    public LiveData<List<Transaction>> getAllData()
    {
        return transactions;
    }
}
