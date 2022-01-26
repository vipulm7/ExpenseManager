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
	public int categoryID, noOfSubCat;

	public int type;

	public SubCategory(String name, int subCatAmount, int subCatBudget, int categoryID, int type) {
		this.name = name;
		this.subCatAmount = subCatAmount;
		this.subCatBudget = subCatBudget;
		this.categoryID = categoryID;
		this.type=type;
		this.noOfSubCat=0;
	}

	public int getId() {
	  return id;
   }
}
