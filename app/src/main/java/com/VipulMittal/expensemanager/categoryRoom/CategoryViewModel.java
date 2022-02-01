package com.VipulMittal.expensemanager.categoryRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepo repo;
    private LiveData<List<Category>> categories;
    private Category category;

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
        categories = repo.getAllCategories(type);
        return categories;
    }

    public Category getCat(int catID)
    {
        category=repo.getCat(catID);
        return category;
    }

    public void UpdateAmt(int increment, int cID)
    {
        repo.UpdateAmt(increment, cID);
    }

}
