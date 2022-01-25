package com.VipulMittal.expensemanager.categoryRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_table")
public class Category {
	@PrimaryKey(autoGenerate = true)
	public int id;

   	public String name;
	public int amount;
	public int budget;
	public int noOfSubCat;
	public int type;

   public Category(String name, int amount, int budget, int noOfSubCat, int type) {
	  this.amount = amount;
	  this.name = name;
	  this.budget = budget;
	  this.noOfSubCat = noOfSubCat;
	  this.type = type;
   }

   public int getId() {
	  return id;
   }
}
