package com.lordscave.ranchshopsimulator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lordscave.ranchshopsimulator.model.CartItem;
import com.lordscave.ranchshopsimulator.model.HouseHold;
import com.lordscave.ranchshopsimulator.utils.CartManager;
import com.lordscave.ranchshopsimulator.utils.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.lordscave.ranchshopsimulator.controller.ShopController.formatBalance;

public class CartController {
    @FXML public Label houseHoldBal,subtotalLabel;
    @FXML public VBox cartListContainer;

    private Stage currentStage;
    private Stage previousStage;

    private int householdBalance;
    private List<CartItem> cartItems;
    private MessageController messageController = new MessageController();
    private ShopController shopController;


    public void setStages(Stage currentStage, Stage previousStage) {
        this.currentStage = currentStage;
        this.previousStage = previousStage;
    }

    public void setShopController(ShopController shopController) {
        this.shopController = shopController;
    }

    public void setCartData(List<CartItem> cartItems, int householdBalance) {
        this.cartItems = cartItems;
        this.householdBalance = householdBalance;
        updateCartUI(false);
    }




    public void removeCartItemRow(CartItemRowController rowController) {
        cartListContainer.getChildren().remove(rowController.getRootNode());
    }


    public void saveBalance(int newBalance) {
        HouseHold houseHold = new HouseHold();
        houseHold.setBalance(newBalance);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(new File("src/main/resources/com/lordscave/ranchshopsimulator/data/house_hold.json"), houseHold);
            houseHoldBal.setText("Household Balance: $" + formatBalance(houseHold.getBalance()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void refreshCart() {
        updateCartUI(true);
    }

    public void handleCheckout(ActionEvent actionEvent) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }

        if (total > householdBalance) {
            messageController.showCustomMessage(MessageType.ERROR,"You do not have enough household balance to checkout.");
            return;
        }

        householdBalance -= total;

        cartItems.clear();
        CartManager.getInstance().clearCart();


        messageController.showCustomMessage(MessageType.SUCCESS,"Your items have been checked out successfully!");

        saveBalance(householdBalance);

        if (shopController != null) {
            shopController.updateHouseholdLabel(householdBalance);
        }

        handleBackButton();

    }

    private void updateCartUI(boolean refreshCart) {
        double total = 0;
        int count = 0;
        if (!refreshCart) {
            houseHoldBal.setText("Household Balance: $" + formatBalance(householdBalance));

            for (CartItem item : cartItems) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lordscave/ranchshopsimulator/components/CartItemRow.fxml"));
                    Node card = loader.load();

                    CartItemRowController controller = loader.getController();
                    controller.setData(item, this);

                    cartListContainer.getChildren().add(card);
                    total += item.getTotalPrice();
                    count += item.getQuantity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            subtotalLabel.setText("Subtotal (" + count + " items) : $" + String.format("%.2f", total));
        }else {
            for (CartItem item : cartItems) {
                total += item.getTotalPrice();
                count += item.getQuantity();
            }
            subtotalLabel.setText("Subtotal (" + count + " items) : $" + String.format("%.2f", total));
        }

    }

    public void handleBackButton() {
        previousStage.show();
        currentStage.close();
    }


}

