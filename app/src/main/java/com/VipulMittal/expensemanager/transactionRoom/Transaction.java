package com.VipulMittal.expensemanager.transactionRoom;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "transaction_table")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int amount;
    public String amountDisplay;
    public int accountID;
    public int catID;
    public int subCatID;
    public String note;
    public String description;
    public int type;
    public long date;
    public int month, dateOfMonth, year, week, day;
    public static final String TAG="Vipul_tag";


    public int getId() {
        return id;
    }

    public Transaction(String note, int amount, String amountDisplay, int accountID, int catID, int subCatID, String description, int type, long date) {
        this.note = note;
        this.amount = amount;
        this.amountDisplay = amountDisplay;
        this.accountID = accountID;
        this.catID = catID;
        this.subCatID = subCatID;
        this.description = description;
        this.type = type;
        this.date=date;
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(date);
        month=calendar.get(Calendar.MONTH);
        dateOfMonth =calendar.get(Calendar.DATE);
        day=calendar.get(Calendar.DAY_OF_WEEK);
        year=calendar.get(Calendar.YEAR);
        week=calendar.get(Calendar.WEEK_OF_YEAR);

        Log.d(TAG, "Transaction:database m="+month+" y="+year);
    }
}
