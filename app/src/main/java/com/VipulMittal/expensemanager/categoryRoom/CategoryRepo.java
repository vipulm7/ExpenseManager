package com.VipulMittal.expensemanager.categoryRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryRepo {
	private final CategoryDAO categoryDAO;

	public CategoryRepo(Application application) {
		CategoryDatabase categoryDatabase = CategoryDatabase.getInstance(application);
		categoryDAO = categoryDatabase.categoryDAO();
	}

	public void Insert(Category category) {
		new InsertNoteASyncTask(categoryDAO).execute(category);
	}

	public void Delete(Category category) {
		new DeleteNoteASyncTask(categoryDAO).execute(category);
	}

	public void Update(Category category) {
		new UpdateNoteASyncTask(categoryDAO).execute(category);
	}

	public LiveData<List<Category>> getAllCategories(int type) {
		return categoryDAO.getAllCategories(type);
	}

	public void catAdded(int catID) {
		categoryDAO.catAdded(catID);
	}

	public void catDeleted(int catID) {
		categoryDAO.catDeleted(catID);
	}

	public Category getCat(int catID) {
		return categoryDAO.getCat(catID);
	}

	public void UpdateAmt(int increment, int cID) {
		categoryDAO.UpdateAmt(increment, cID);
	}


	private static class InsertNoteASyncTask extends AsyncTask<Category, Void, Void> {
		private final CategoryDAO transactionDAO;

		private InsertNoteASyncTask(CategoryDAO transactionDAO) {
			this.transactionDAO = transactionDAO;
		}

		@Override
		protected Void doInBackground(Category... categories) {
			this.transactionDAO.Insert(categories[0]);
			return null;
		}
	}

	private static class DeleteNoteASyncTask extends AsyncTask<Category, Void, Void> {
		private final CategoryDAO transactionDAO;

		private DeleteNoteASyncTask(CategoryDAO transactionDAO) {
			this.transactionDAO = transactionDAO;
		}

		@Override
		protected Void doInBackground(Category... categories) {
			this.transactionDAO.Delete(categories[0]);
			return null;
		}
	}

	private static class UpdateNoteASyncTask extends AsyncTask<Category, Void, Void> {
		private final CategoryDAO transactionDAO;

		private UpdateNoteASyncTask(CategoryDAO transactionDAO) {
			this.transactionDAO = transactionDAO;
		}

		@Override
		protected Void doInBackground(Category... categories) {
			this.transactionDAO.Update(categories[0]);
			return null;
		}
	}
}
