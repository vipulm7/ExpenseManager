package com.VipulMittal.expensemanager.dateRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DateDAO {
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void Insert(Date date);

	@Update
	void Update(Date date);

	@Delete
	void Delete(Date date);

	@Query("SELECT * FROM date_table ORDER BY date DESC")
	LiveData<List<Date>> getDates();
}
