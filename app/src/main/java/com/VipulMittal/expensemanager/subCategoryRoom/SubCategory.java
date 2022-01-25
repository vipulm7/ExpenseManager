package com.VipulMittal.expensemanager.subCategoryRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subcategory_table")
public class SubCategory {
	@PrimaryKey(autoGenerate = true)
	public int id;


   	public String name;
	public int amount;
	public int budget;
	public int noOfSubCat;
	public int categoryID;
	public int type;

	public SubCategory(String name, int amount, int budget, int categoryID, int type) {
		this.name = name;
		this.amount = amount;
		this.budget = budget;
		this.noOfSubCat = 0;
		this.categoryID = categoryID;
		this.type=type;
	}

	public int getId() {
	  return id;
   }
}
