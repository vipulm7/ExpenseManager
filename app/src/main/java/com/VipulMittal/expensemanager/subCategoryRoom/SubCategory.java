package com.VipulMittal.expensemanager.subCategoryRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subcategory_table")
public class SubCategory {
	@PrimaryKey(autoGenerate = true)
	public int id;

   	public String name;
	public int subCatAmount;
	public int subCatBudget;
	public int categoryID;
	public int type;
	public int subCatImageID;

	public SubCategory(String name, int subCatAmount, int subCatBudget, int categoryID, int type, int subCatImageID) {
		this.name = name;
		this.subCatAmount = subCatAmount;
		this.subCatBudget = subCatBudget;
		this.categoryID = categoryID;
		this.type=type;
		this.subCatImageID = subCatImageID;
	}

	public int getId() {
	  return id;
   }
}
