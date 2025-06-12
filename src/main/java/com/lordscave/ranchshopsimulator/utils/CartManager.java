package com.lordscave.ranchshopsimulator.utils;

import com.lordscave.ranchshopsimulator.model.CartItem;
import com.lordscave.ranchshopsimulator.model.ShopItem;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems = new ArrayList<>();
    private int householdBalance;

    private CartManager(){}

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(ShopItem item){
        for (CartItem cartItem : cartItems) {
            if (cartItem.getItem().getName().equals(item.getName())) {
                cartItem.incrementQuantity();
                return;
            }
        }
        cartItems.add(new CartItem(item, 1));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void removeItem(CartItem item) {
        cartItems.remove(item);
    }
}
