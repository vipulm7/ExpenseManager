package com.VipulMittal.expensemanager.categoryRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface CategoryDAO {

    @Insert
    void Insert(Category category);

    @Update
    void Update(Category category);

    @Delete
    void Delete(Category category);

    @Query("SELECT * FROM category_table WHERE type = :type ORDER BY name ASC")
    LiveData<List<Category>> getAllCategories(int type);

    @Query("UPDATE category_table SET noOfSubCat=noOfSubCat+1 WHERE id=:catID")
    void catAdded(int catID);

    @Query("UPDATE category_table SET noOfSubCat=noOfSubCat-1 WHERE id=:catID")
    void catDeleted(int catID);
}
