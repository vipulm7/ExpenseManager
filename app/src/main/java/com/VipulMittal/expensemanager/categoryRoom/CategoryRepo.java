package com.VipulMittal.expensemanager.categoryRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryRepo
{
    private CategoryDAO categoryDAO;
    private LiveData<List<Category>> transactions;

    public CategoryRepo(Application application)
    {
        CategoryDatabase categoryDatabase = CategoryDatabase.getInstance(application);
        categoryDAO = categoryDatabase.transactionDAO();
        transactions= categoryDAO.getAllData();
    }

    public void Insert (Category category)
    {
        new InsertNoteASyncTask(categoryDAO).execute(category);
    }

    public void Delete(Category category)
    {
        new DeleteNoteASyncTask(categoryDAO).execute(category);
    }

    public void Update(Category category)
    {
        new UpdateNoteASyncTask(categoryDAO).execute(category);
    }

    public LiveData<List<Category>> getAllData()
    {
        return transactions;
    }


    private static class InsertNoteASyncTask extends AsyncTask<Category,Void,Void>
    {
        private CategoryDAO categoryDAO;

        private InsertNoteASyncTask(CategoryDAO categoryDAO)
        {
            this.categoryDAO = categoryDAO;
        }
        @Override
        protected Void doInBackground(Category... categories) {
            this.categoryDAO.Insert(categories[0]);
            return null;
        }
    }

    private static class DeleteNoteASyncTask extends AsyncTask<Category,Void,Void>
    {
        private CategoryDAO categoryDAO;

        private DeleteNoteASyncTask(CategoryDAO categoryDAO)
        {
            this.categoryDAO = categoryDAO;
        }
        @Override
        protected Void doInBackground(Category... categories) {
            this.categoryDAO.Delete(categories[0]);
            return null;
        }
    }

    private static class UpdateNoteASyncTask extends AsyncTask<Category,Void,Void>
    {
        private CategoryDAO categoryDAO;

        private UpdateNoteASyncTask(CategoryDAO categoryDAO)
        {
            this.categoryDAO = categoryDAO;
        }
        @Override
        protected Void doInBackground(Category... categories) {
            this.categoryDAO.Update(categories[0]);
            return null;
        }
    }
}
