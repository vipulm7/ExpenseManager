package com.VipulMittal.expensemanager.categoryRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepo repo;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repo =new CategoryRepo(application);
    }

    public void Insert(Category category)
    {
        repo.Insert(category);
    }

    public void Delete(Category category)
    {
        repo.Delete(category);
    }

    public void Update(Category category)
    {
        repo.Update(category);
    }

    public LiveData<List<Category>> getAllCategories(int type)
    {
        return repo.getAllCategories(type);
    }

    public Category getCat(int catID)
    {
        return repo.getCat(catID);
    }

    public void UpdateAmt(int increment, int cID)
    {
        repo.UpdateAmt(increment, cID);
    }

    public void catAdded(int catID)
    {
        repo.catAdded(catID);
    }

    public void catDeleted(int catID)
    {
        repo.catDeleted(catID);
    }

}
