package com.VipulMittal.expensemanager.subCategoryRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SubCategoryViewModel extends AndroidViewModel {

    private SubCategoryRepo repo;
    private LiveData<List<SubCategory>> subCategories;

    public SubCategoryViewModel(@NonNull Application application) {
        super(application);
        repo =new SubCategoryRepo(application);
        subCategories= repo.getAllSubCategories();
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

    public LiveData<List<SubCategory>> getAllSubCategories()
    {
        return subCategories;
    }
}
