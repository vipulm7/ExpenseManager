package com.VipulMittal.expensemanager.transactionRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDAO {

	@Insert
	void Insert(Transaction transaction);

	@androidx.room.Transaction
	@Insert
	void InsertTransactions(Transaction... transactions);

	@Update
	void Update(Transaction transaction);

	@Delete
	void Delete(Transaction transaction);

	@Query("SELECT * FROM transaction_table ORDER BY dateTime DESC")
	LiveData<List<Transaction>> getAllTransactions();

	@Query("SELECT * FROM transaction_table WHERE accountID=:accountID")
	List<Transaction> getAllTransactionsAcc(int accountID);

	@Query("SELECT * FROM transaction_table WHERE catID=:catID")
	List<Transaction> getAllTransactionsCat(int catID);

	@Query("SELECT * FROM transaction_table WHERE subCatID=:subCatID")
	List<Transaction> getAllTransactionsSubCat(int subCatID);
}
