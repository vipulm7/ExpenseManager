package com.VipulMittal.expensemanager.subCategoryRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.Query;
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
    @Query("SELECT * FROM category_table JOIN subcategory_table ON category_table.id = subcategory_table.categoryID")
    LiveData<Map<Category, List<SubCategory>>> getAllSubCategories();

//    @Query("SELECT * AS subCat, category_table.* AS cat FROM subcategory_table, category_table WHERE categoryID=:catID")
//    LiveData<List<SubCategory>> getSubs(int catID);
}
