package com.lordscave.ranchshopsimulator.controller;

import com.lordscave.ranchshopsimulator.model.CartItem;
import com.lordscave.ranchshopsimulator.utils.CartManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.io.InputStream;

public class CartItemRowController {
    @FXML public Label itemName,itemPrice,itemQuantity;
    @FXML public ImageView itemImage;
    @FXML public HBox cartItemRow;

    private CartItem cartItem;
    private CartController cartController;


    public Node getRootNode() {
        return cartItemRow;
    }


    public void setData(CartItem item, CartController controller) {
        this.cartItem = item;
        this.cartController = controller;
        itemName.setText(item.getName());
        itemPrice.setText("$" + String.format("%.2f", item.getTotalPrice()));
        itemQuantity.setText(String.valueOf(item.getQuantity()));

        String imagePath = "/" + item.getImagePath();
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream == null) {
            imageStream = getClass().getResourceAsStream("/com/lordscave/ranchshopsimulator/itemimages/animal/Default.png");
        }
        if (imageStream != null) {
            itemImage.setImage(new Image(imageStream));
            try {
                imageStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void decreaseQuantity(ActionEvent actionEvent) {
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            updateUI();
            cartController.refreshCart();
        } else {
            CartManager.getInstance().removeItem(cartItem);
            cartController.removeCartItemRow(this);  // remove UI node
            cartController.refreshCart();
        }

    }

    public void increaseQuantity(ActionEvent actionEvent) {
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartController.refreshCart();
        updateUI();
    }

    private void updateUI() {
        itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
        itemPrice.setText("$" + String.format("%.2f", cartItem.getTotalPrice()));

    }
}

