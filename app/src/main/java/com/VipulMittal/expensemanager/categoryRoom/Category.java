package com.VipulMittal.expensemanager.categoryRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_table")
public class Category {
	@PrimaryKey(autoGenerate = true)
	public int catId;

	public String catName;
	public int catAmount;
	public int catBudget;
	public int noOfSubCat;
	public int type;
	public int catImageID;

	public Category(String catName, int catAmount, int catBudget, int noOfSubCat, int type, int catImageID) {
		this.catAmount = catAmount;
		this.catName = catName;
		this.catBudget = catBudget;
		this.noOfSubCat = noOfSubCat;
		this.type = type;
		this.catImageID = catImageID;
	}

	public int getCatId() {
		return catId;
	}
}
