package com.VipulMittal.expensemanager.transactionRoom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;
//@Fts4
@Entity(tableName = "transaction_table")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "rowid")
    public int id;

    public String note;
    public int amount;
    public String amountDisplay;
    public String account;
    public String cat;
    public String subCat;
    public String description;
    public int type;
    public long date;


    public int getId() {
        return id;
    }

    public Transaction(String note, int amount, String amountDisplay, String account, String cat, String subCat, String description, long date, int type) {
        this.note = note;
        this.amount = amount;
        this.amountDisplay = amountDisplay;
        this.account = account;
        this.cat = cat;
        this.subCat = subCat;
        this.description = description;
        this.date = date;
        this.type=type;
    }
}
