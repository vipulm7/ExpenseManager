package com.VipulMittal.expensemanager.subCategoryRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.VipulMittal.expensemanager.categoryRoom.Category;

import java.util.List;
import java.util.Map;

@Dao
public interface SubCategoryDAO {

    @Insert
    void Insert(SubCategory subCategory);

    @Update
    void Update(SubCategory subCategory);

    @Delete
    void Delete(SubCategory subCategory);

//    @Query("SELECT * FROM subcategory_table")
//    LiveData<List<SubCategory>> getAllSubCategories();

    @MapInfo(keyColumn = "id")
    @Query("SELECT * FROM category_table JOIN subcategory_table ON catId = categoryID")
    LiveData<Map<Category, List<SubCategory>>> getAllSubCategories();

//    @Transaction
//    @Query("SELECT * FROM category_table")
//    LiveData<List<mix>> getSubCats();

    @Query("SELECT * FROM subcategory_table WHERE categoryID=:catID")
    LiveData<List<SubCategory>> getSubs(int catID);

    @Query("SELECT * FROM subcategory_table")
    LiveData<List<SubCategory>> getAllSubcats();
}
