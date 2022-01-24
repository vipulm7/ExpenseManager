package com.VipulMittal.expensemanager.subCategoryRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.VipulMittal.expensemanager.categoryRoom.Category;

import java.util.List;
import java.util.Map;

public class SubCategoryRepo
{
    private SubCategoryDAO subCategoryDAO;
    private LiveData<Map<Category, List<SubCategory>>> subCategories;

    public SubCategoryRepo(Application application)
    {
        SubCategoryDatabase subCategoryDatabase = SubCategoryDatabase.getInstance(application);
        subCategoryDAO = subCategoryDatabase.subCategoryDAO();
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

    public LiveData<Map<Category, List<SubCategory>>> getAllSubCategories(int type)
    {
        subCategories = subCategoryDAO.getAllSubCategories(type);
        return subCategories;
    }


    private static class InsertNoteASyncTask extends AsyncTask<SubCategory,Void,Void>
    {
        private SubCategoryDAO subCategoryDAO;

        private InsertNoteASyncTask(SubCategoryDAO subCategoryDAO)
        {
            this.subCategoryDAO = subCategoryDAO;
        }
        @Override
        protected Void doInBackground(SubCategory... subCategories) {
            this.subCategoryDAO.Insert(subCategories[0]);
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
        protected Void doInBackground(SubCategory... subCategories) {
            this.subCategoryDAO.Delete(subCategories[0]);
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
        protected Void doInBackground(SubCategory... subCategories) {
            this.subCategoryDAO.Update(subCategories[0]);
            return null;
        }
    }
}
