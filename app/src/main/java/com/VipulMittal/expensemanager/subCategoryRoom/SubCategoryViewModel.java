package com.VipulMittal.expensemanager.subCategoryRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.VipulMittal.expensemanager.categoryRoom.Category;

import java.util.List;
import java.util.Map;

public class SubCategoryViewModel extends AndroidViewModel {

    private SubCategoryRepo repo;
    private LiveData<Map<Category, List<SubCategory>>> subCategories;

    public SubCategoryViewModel(@NonNull Application application) {
        super(application);
        repo =new SubCategoryRepo(application);
    }

    public void Insert(SubCategory subCategory)
    {
        repo.Insert(subCategory);
    }

    public void Delete(SubCategory subCategory)
    {
        repo.Delete(subCategory);
    }

    public void Update(SubCategory subCategory)
    {
        repo.Update(subCategory);
    }

    public LiveData<Map<Category, List<SubCategory>>> getAllSubCategories(int type)
    {
        subCategories= repo.getAllSubCategories(type);
        return subCategories;
    }
}
