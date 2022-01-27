package com.VipulMittal.expensemanager.transactionRoom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import com.VipulMittal.expensemanager.dateRoom.Date;
import com.google.gson.Gson;

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
//    public long date;
    public long date;


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
    }
}
