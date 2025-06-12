package com.lordscave.ranchshopsimulator.model;

public class HouseHold {
    private int balance;

    private int storage;

    public HouseHold() {
    }

    public int getStorage() {
        return storage;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance,int storage) {
        this.balance = balance;
        this.storage = storage;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }



}
