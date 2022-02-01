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

//    @Query("SELECT * FROM transaction_table ORDER BY date DESC")
//    @Query("SELECT * FROM transaction_table")
    @Query("SELECT * FROM transaction_table WHERE month=:month AND year=:year ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions(int month, int year);

    @Query("UPDATE transaction_table SET note=:note, amount=:amount, amountDisplay=:amountDisplay, accountID=:accountID, catID=:catID, subCatID=:subCatID, description=:description, type=:type, date=:date, dateTime=:dateTime WHERE id=:id")
    void UpdateTrans(String note, int amount, String amountDisplay, int accountID, int catID, int subCatID, String description, int type, long date,long dateTime, int id);
}
