package com.VipulMittal.expensemanager.subCategoryRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SubCategoryDAO {

    @Insert
    void Insert(SubCategory subCategory);

    @Update
    void Update(SubCategory subCategory);

    @Delete
    void Delete(SubCategory subCategory);

    @Query("SELECT * FROM subcategory_table")
    LiveData<List<SubCategory>> getAllSubCategories();


}
