package com.VipulMittal.expensemanager.accountRoom.transactionRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.VipulMittal.expensemanager.transactionRoom.Transaction;

import java.util.List;

@Dao
public interface TransactionDAO {

    @Insert
    void Insert(com.VipulMittal.expensemanager.accountRoom.transactionRoom.Transaction transaction);

    @Update
    void Update(com.VipulMittal.expensemanager.accountRoom.transactionRoom.Transaction transaction);

    @Delete
    void Delete(com.VipulMittal.expensemanager.accountRoom.transactionRoom.Transaction transaction);

    @Query("SELECT * FROM transaction_table ORDER BY date DESC")
    LiveData<List<com.VipulMittal.expensemanager.accountRoom.transactionRoom.Transaction>> getAllData();


}
