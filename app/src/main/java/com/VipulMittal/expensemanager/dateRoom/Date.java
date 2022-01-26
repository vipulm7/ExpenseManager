package com.VipulMittal.expensemanager.dateRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "date_table")
public class Date {
	@PrimaryKey(autoGenerate = true)
	public int id;

	public long millies;
	public Calendar calendar;
	public int month, date, year, week, day;
	public int transactionsOnThatDate;

	public Date(long millies) {
		this.millies = millies;
		calendar=Calendar.getInstance();
		calendar.setTimeInMillis(millies);
		month=calendar.MONTH;
		date=calendar.DATE;
		day=Calendar.DAY_OF_WEEK;
		year=Calendar.YEAR;
		week=Calendar.WEEK_OF_YEAR;
		this.transactionsOnThatDate = 0;
	}
}
