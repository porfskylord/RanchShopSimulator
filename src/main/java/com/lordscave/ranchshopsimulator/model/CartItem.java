package com.lordscave.ranchshopsimulator.model;

public class CartItem {
    private ShopItem item;
    private int quantity;

    public CartItem(ShopItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public ShopItem getItem() {
        return item;
    }

    public String getName(){
        return item.getName();
    }

    public int getQuantity() {
        return quantity;
    }

    public void incrementQuantity(){
        this.quantity++;
    }

    public double getTotalPrice(){
        return item.getPrice() * quantity;
    }

    public String getImagePath() {
        return item.getImagePath();
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }
}
