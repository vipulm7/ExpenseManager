package com.VipulMittal.expensemanager.subCategoryRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SubCategoryRepo
{
    private SubCategoryDAO subCategoryDAO;
    private LiveData<List<SubCategory>> subcategories;

    public SubCategoryRepo(Application application)
    {
        SubCategoryDatabase subCategoryDatabase = SubCategoryDatabase.getInstance(application);
        subCategoryDAO = subCategoryDatabase.subCategoryDAO();
        subcategories = subCategoryDAO.getAllData();
    }

    public void Insert (SubCategory subCategory)
    {
        new InsertNoteASyncTask(subCategoryDAO).execute(subCategory);
    }

    public void Delete(SubCategory subCategory)
    {
        new DeleteNoteASyncTask(subCategoryDAO).execute(subCategory);
    }

    public void Update(SubCategory subCategory)
    {
        new UpdateNoteASyncTask(subCategoryDAO).execute(subCategory);
    }

    public LiveData<List<SubCategory>> getAllData()
    {
        return subcategories;
    }


    private static class InsertNoteASyncTask extends AsyncTask<SubCategory,Void,Void>
    {
        private SubCategoryDAO subCategoryDAO;

        private InsertNoteASyncTask(SubCategoryDAO subCategoryDAO)
        {
            this.subCategoryDAO = subCategoryDAO;
        }
        @Override
        protected Void doInBackground(SubCategory... categories) {
            this.subCategoryDAO.Insert(categories[0]);
            return null;
        }
    }

    private static class DeleteNoteASyncTask extends AsyncTask<SubCategory,Void,Void>
    {
        private SubCategoryDAO subCategoryDAO;

        private DeleteNoteASyncTask(SubCategoryDAO subCategoryDAO)
        {
            this.subCategoryDAO = subCategoryDAO;
        }
        @Override
        protected Void doInBackground(SubCategory... categories) {
            this.subCategoryDAO.Delete(categories[0]);
            return null;
        }
    }

    private static class UpdateNoteASyncTask extends AsyncTask<SubCategory,Void,Void>
    {
        private SubCategoryDAO subCategoryDAO;

        private UpdateNoteASyncTask(SubCategoryDAO subCategoryDAO)
        {
            this.subCategoryDAO = subCategoryDAO;
        }
        @Override
        protected Void doInBackground(SubCategory... categories) {
            this.subCategoryDAO.Update(categories[0]);
            return null;
        }
    }
}
