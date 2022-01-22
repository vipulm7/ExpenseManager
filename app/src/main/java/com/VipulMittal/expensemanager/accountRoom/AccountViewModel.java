package com.VipulMittal.expensemanager.accountRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {

    private AccountRepo repo;
    private LiveData<List<Account>> accounts;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        repo =new AccountRepo(application);
        accounts= repo.getAllData();
    }

    public void Insert(Account account)
    {
        repo.Insert(account);
    }

    public void Delete(Account account)
    {
        repo.Delete(account);
    }

    public void Update(Account account)
    {
        repo.Update(account);
    }

    public LiveData<List<Account>> getAllData()
    {
        return accounts;
    }
}
