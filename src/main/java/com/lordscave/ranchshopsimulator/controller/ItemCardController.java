package com.lordscave.ranchshopsimulator.controller;

import com.lordscave.ranchshopsimulator.model.ShopItem;
import com.lordscave.ranchshopsimulator.utils.CartManager;
import com.lordscave.ranchshopsimulator.utils.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class ItemCardController {
    @FXML public Label itemName,itemPrice;
    @FXML public ImageView itemImage;

    private MessageController messageController = new MessageController();

    private ShopItem shopItem;

    public void setData(ShopItem item) {
        this.shopItem = item;
        itemName.setText(item.getName());
        itemPrice.setText("$" + item.getPrice());
        String imagePath = "/" + item.getImagePath();
        InputStream stream = getClass().getResourceAsStream(imagePath);

        if (stream == null) {
            stream = getClass().getResourceAsStream("/com/lordscave/ranchshopsimulator/itemimages/animal/Default.png");
        }
        if (stream != null) {
            Image image = new Image(stream);
            itemImage.setImage(image);
        } else {
            messageController.showCustomMessage(MessageType.ERROR,"Contact Admin");
        }
    }


    public void handleAddToCart(ActionEvent actionEvent) {
        CartManager.getInstance().addToCart(shopItem);
        ShopController.updateCartStatic();
    }
}
