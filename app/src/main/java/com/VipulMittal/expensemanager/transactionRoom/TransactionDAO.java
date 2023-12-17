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

	@Update
	void Update(Transaction transaction);

	@Delete
	void Delete(Transaction transaction);

	@Query("SELECT * FROM transaction_table WHERE month=:month AND year=:year ORDER BY dateTime DESC")
	LiveData<List<Transaction>> getAllTransactionsMONTH(int month, int year);

	@Query("SELECT * FROM transaction_table WHERE week=:week AND year=:year ORDER BY dateTime DESC")
	LiveData<List<Transaction>> getAllTransactionsWEEK(int week, int year);

	@Query("SELECT * FROM transaction_table WHERE dateOfMonth=:day AND month=:month AND year=:year ORDER BY dateTime DESC")
	LiveData<List<Transaction>> getAllTransactionsDAY(int day, int month, int year);

	@Query("SELECT * FROM transaction_table WHERE accountID=:accountID")
	List<Transaction> getAllTransactionsAcc(int accountID);

	@Query("SELECT * FROM transaction_table WHERE catID=:catID")
	List<Transaction> getAllTransactionsCat(int catID);

	@Query("SELECT * FROM transaction_table WHERE subCatID=:subCatID")
	List<Transaction> getAllTransactionsSubCat(int subCatID);
}
