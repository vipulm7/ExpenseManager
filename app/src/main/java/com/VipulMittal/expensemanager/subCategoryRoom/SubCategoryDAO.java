package com.VipulMittal.expensemanager.subCategoryRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
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

    @Query("SELECT * FROM category_table JOIN subcategory_table ON category_table.id = subcategory_table.categoryID WHERE subcategory_table.type=:type")
    LiveData<Map<Category, List<SubCategory>>> getAllSubCategories(int type);
    //   GROUP BY subcategory_table.categoryID ORDER BY subcategory_table.name ASC

//    @Query("UPDATE category_table SET noOfSubCat = (SELECT COUNT (categoryID) FROM subcategory_table)")
//    void updateNo();
//    //subcategory_table.categoryID
}
