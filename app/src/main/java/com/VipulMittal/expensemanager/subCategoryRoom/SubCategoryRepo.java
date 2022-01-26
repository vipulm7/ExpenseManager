package com.VipulMittal.expensemanager.subCategoryRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryDAO;
import com.VipulMittal.expensemanager.categoryRoom.CategoryDatabase;

import java.util.List;
import java.util.Map;

public class SubCategoryRepo
{
    private SubCategoryDAO subCategoryDAO;
    private CategoryDAO categoryDAO;
    private LiveData<Map<Category, List<SubCategory>>> subCategories;
//    private LiveData<List<SubCategory>> subs;
//    private LiveData<List<mix>> subs;
    private LiveData<List<SubCategory>> subs;

    public SubCategoryRepo(Application application)
    {
        SubCategoryDatabase subCategoryDatabase = SubCategoryDatabase.getInstance(application);
        subCategoryDAO = subCategoryDatabase.subCategoryDAO();
        CategoryDatabase categoryDatabase=CategoryDatabase.getInstance(application);
        categoryDAO=categoryDatabase.categoryDAO();
    }

    public void Insert (SubCategory subCategory)
    {
        new InsertNoteASyncTask(subCategoryDAO).execute(subCategory);
        categoryDAO.catAdded(subCategory.categoryID);
    }

    public void Delete(SubCategory subCategory)
    {
        new DeleteNoteASyncTask(subCategoryDAO).execute(subCategory);
        categoryDAO.catDeleted(subCategory.categoryID);
    }

    public void Update(SubCategory subCategory)
    {
        new UpdateNoteASyncTask(subCategoryDAO).execute(subCategory);
    }

//    public LiveData<List<mix>> getSubCats()
//    {
//        subs = subCategoryDAO.getSubCats();
//        return subs;
//    }

    public LiveData<Map<Category, List<SubCategory>>> getAllSubCategories(int type)
    {
        subCategories = subCategoryDAO.getAllSubCategories();
        return subCategories;
    }

    LiveData<List<SubCategory>> getSubs(int catID)
    {
        subs= subCategoryDAO.getSubs(catID);
        return subs;
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
