package com.VipulMittal.expensemanager.transactionRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

	private final TransactionRepo repo;
	private LiveData<List<Transaction>> transactions;

	public TransactionViewModel(@NonNull Application application) {
		super(application);
		repo = new TransactionRepo(application);
	}

	public void Insert(Transaction transaction) {
		repo.Insert(transaction);
	}

	public void Delete(Transaction transaction) {
		repo.Delete(transaction);
	}

	public void Update(Transaction transaction) {
		repo.Update(transaction);
	}

	public LiveData<List<Transaction>> getAllTransactions() {
		return repo.getAllTransactions();
	}

	public List<Transaction> getAllTransactionsAcc(int accountID) {
		return repo.getAllTransactionsAcc(accountID);
	}

	public List<Transaction> getAllTransactionsCat(int catID) {
		return repo.getAllTransactionsCat(catID);
	}

	public List<Transaction> getAllTransactionsSubCat(int subCatID) {
		return repo.getAllTransactionsSubCat(subCatID);
	}
}
