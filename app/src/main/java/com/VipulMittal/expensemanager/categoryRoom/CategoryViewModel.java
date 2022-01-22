package com.VipulMittal.expensemanager.categoryRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepo repo;
    private LiveData<List<Category>> transactions;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repo =new CategoryRepo(application);
        transactions= repo.getAllData();
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

    public LiveData<List<Category>> getAllData()
    {
        return transactions;
    }
}
