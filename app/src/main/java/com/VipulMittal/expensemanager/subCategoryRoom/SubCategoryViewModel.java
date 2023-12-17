package com.VipulMittal.expensemanager.subCategoryRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.VipulMittal.expensemanager.categoryRoom.Category;

import java.util.List;
import java.util.Map;

public class SubCategoryViewModel extends AndroidViewModel {

	private final SubCategoryRepo repo;
	private LiveData<Map<Category, List<SubCategory>>> subCategories;
	private LiveData<List<SubCategory>> subs;
	private LiveData<List<SubCategory>> allSubs;

	public SubCategoryViewModel(@NonNull Application application) {
		super(application);
		repo = new SubCategoryRepo(application);
	}

	public void Insert(SubCategory subCategory) {
		repo.Insert(subCategory);
	}

	public void Delete(SubCategory subCategory) {
		repo.Delete(subCategory);
	}

	public void Update(SubCategory subCategory) {
		repo.Update(subCategory);
	}

	public LiveData<Map<Category, List<SubCategory>>> getAllSubCategories(int type) {
		subCategories = repo.getAllSubCategories(type);
		return subCategories;
	}

	public LiveData<List<SubCategory>> getSubcategories(int catID) {
		subs = repo.getSubcategories(catID);
		return subs;
	}

	public LiveData<List<SubCategory>> getAllSubcats() {
		allSubs = repo.getAllSubcats();
		return allSubs;
	}

	public List<SubCategory> getSubcats(int catID) {
		return repo.getSubcats(catID);
	}

	public SubCategory getSubCat(int sID) {
		return repo.getSubCat(sID);
	}

	public void UpdateAmt(int increment, int sID) {
		repo.UpdateAmt(increment, sID);
	}
}
