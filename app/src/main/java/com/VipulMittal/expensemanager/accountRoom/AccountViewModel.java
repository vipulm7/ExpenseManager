package com.VipulMittal.expensemanager.accountRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {

	public AccountRepo repo;
	private LiveData<List<Account>> accounts;

	public AccountViewModel(@NonNull Application application) {
		super(application);
		repo = new AccountRepo(application);
	}

	public void Insert(Account account) {
		repo.Insert(account);
	}

	public void Delete(Account account) {
		repo.Delete(account);
	}

	public void Update(Account account) {
		repo.Update(account);
	}

	public LiveData<List<Account>> getAllAccounts() {
		accounts = repo.getAllAccounts();
		return accounts;
	}

	public Account getAcc(int aID) {
		return repo.getAcc(aID);
	}

	public void UpdateAmt(int increment, int aID) {
		repo.UpdateAmt(increment, aID);
	}
}
