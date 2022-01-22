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

    @Query("SELECT * FROM transaction_table ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions();


}
