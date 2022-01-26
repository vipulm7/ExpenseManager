package com.VipulMittal.expensemanager.categoryRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_table")
public class Category {
	@PrimaryKey(autoGenerate = true)
	public int id;

   	public String catName;
	public int catAmount;
	public int catBudget;
	public int noOfSubCat;
	public int type;

   public Category(String catName, int catAmount, int catBudget, int noOfSubCat, int type) {
	  this.catAmount = catAmount;
	  this.catName = catName;
	  this.catBudget = catBudget;
	  this.noOfSubCat = noOfSubCat;
	  this.type = type;
   }

   public int getId() {
	  return id;
   }
}
