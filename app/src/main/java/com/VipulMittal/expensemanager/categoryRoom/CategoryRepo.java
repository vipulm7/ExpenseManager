package com.VipulMittal.expensemanager.categoryRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryRepo
{
    private CategoryDAO categoryDAO;
    private LiveData<List<Category>> categories;

    public CategoryRepo(Application application)
    {
        CategoryDatabase categoryDatabase = CategoryDatabase.getInstance(application);
        categoryDAO = categoryDatabase.categoryDAO();
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

    public LiveData<List<Category>> getAllCategories(int type)
    {
        categories = categoryDAO.getAllCategories(type);
        return categories;
    }


    private static class InsertNoteASyncTask extends AsyncTask<Category,Void,Void>
    {
        private CategoryDAO transactionDAO;

        private InsertNoteASyncTask(CategoryDAO transactionDAO)
        {
            this.transactionDAO=transactionDAO;
        }
        @Override
        protected Void doInBackground(Category... categories) {
            this.transactionDAO.Insert(categories[0]);
            return null;
        }
    }

    private static class DeleteNoteASyncTask extends AsyncTask<Category,Void,Void>
    {
        private CategoryDAO transactionDAO;

        private DeleteNoteASyncTask(CategoryDAO transactionDAO)
        {
            this.transactionDAO=transactionDAO;
        }
        @Override
        protected Void doInBackground(Category... categories) {
            this.transactionDAO.Delete(categories[0]);
            return null;
        }
    }

    private static class UpdateNoteASyncTask extends AsyncTask<Category,Void,Void>
    {
        private CategoryDAO transactionDAO;

        private UpdateNoteASyncTask(CategoryDAO transactionDAO)
        {
            this.transactionDAO=transactionDAO;
        }
        @Override
        protected Void doInBackground(Category... categories) {
            this.transactionDAO.Update(categories[0]);
            return null;
        }
    }
}
