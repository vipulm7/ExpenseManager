package com.VipulMittal.expensemanager.accountRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "account_table")
public class Account {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String amountDisplay;
    public int amount;


    public int getId() {
        return id;
    }

    public Account(String name) {
        this.name = name;
        this.amount = 0;
        this.amountDisplay = "0";
    }
}
