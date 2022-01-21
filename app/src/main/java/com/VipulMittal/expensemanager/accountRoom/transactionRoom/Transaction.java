package com.VipulMittal.expensemanager.accountRoom.transactionRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaction_table")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String note;
    public int amount;
    public String amountDisplay;
    public String account;
    public String cat;
    public String subCat;
    public String description;
    public long date;


    public int getId() {
        return id;
    }

    public Transaction(String note, int amount, String amountDisplay, String account, String cat, String subCat, String description, long date) {
        this.note = note;
        this.amount = amount;
        this.amountDisplay = amountDisplay;
        this.account = account;
        this.cat = cat;
        this.subCat = subCat;
        this.description = description;
        this.date = date;
    }
}
