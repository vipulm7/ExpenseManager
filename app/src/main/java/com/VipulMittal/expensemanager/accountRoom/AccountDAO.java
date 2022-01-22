package com.VipulMittal.expensemanager.accountRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDAO {

   @Insert
   void Insert(Account account);

   @Update
   void Update(Account account);

   @Delete
   void Delete(Account account);

   @Query("SELECT * FROM account_table")
   LiveData<List<Account>> getAllData();


}
