package com.VipulMittal.expensemanager.dateRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "date_table")
public class Date {
	@PrimaryKey(autoGenerate = true)
	public int id;

	public long millies;
	public int month, date, year, week, day;
	public int transactionsOnThatDate;

	public Date(long millies) {
		this.millies = millies;
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(millies);
		month=calendar.get(Calendar.MONTH);
		date=calendar.get(Calendar.DATE);
		day=calendar.get(Calendar.DAY_OF_WEEK);
		year=calendar.get(Calendar.YEAR);
		week=calendar.get(Calendar.WEEK_OF_YEAR);;
		this.transactionsOnThatDate = 0;
	}
}
