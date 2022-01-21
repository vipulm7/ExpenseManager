package com.VipulMittal.expensemanager.dateRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "date_table")
public class Date {
	@PrimaryKey(autoGenerate = true)
	public int id;

	public long date;
	public int transactionsOnThatDate;

	public Date(long date) {
		this.date = date;
		this.transactionsOnThatDate = 0;
	}
}
