package com.VipulMittal.expensemanager.accountRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "account_table")
public class Account {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int amount;


    public int getId() {
        return id;
    }

    public Account(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}
