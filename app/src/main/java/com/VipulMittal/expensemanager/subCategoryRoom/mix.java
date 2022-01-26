package com.VipulMittal.expensemanager.subCategoryRoom;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.VipulMittal.expensemanager.categoryRoom.Category;

import java.util.List;

public class mix
{
	@Embedded public Category category;
	@Relation(
			parentColumn = "catId",
			entityColumn = "categoryID"
	)
	public List<SubCategory> getSubCats;
}
