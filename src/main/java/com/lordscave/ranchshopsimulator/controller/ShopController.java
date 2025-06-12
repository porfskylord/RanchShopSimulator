package com.lordscave.ranchshopsimulator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lordscave.ranchshopsimulator.model.CartItem;
import com.lordscave.ranchshopsimulator.model.HouseHold;
import com.lordscave.ranchshopsimulator.model.ShopItem;
import com.lordscave.ranchshopsimulator.utils.CartManager;
import com.lordscave.ranchshopsimulator.utils.ItemLoader;
import com.lordscave.ranchshopsimulator.utils.MessageType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ShopController {


    @FXML public Label hBalance;
    @FXML public Button cart;
    @FXML private ToggleGroup categoryGroup;
    @FXML public TilePane itemTilePane;
    @FXML public ToggleButton allItem, animalItem, foodAndWater, toolsItem, farmingItem, gardernItem, foodProdItem;

    private static ShopController instance;
    private int houseHoldBalance;
    private MessageController messageController = new MessageController();

    public ShopController() {
        instance = this;
    }

    @FXML
    public void initialize(){
        getHouseHoldData();
        setToggleColor();
        getShopItems(null);
    }

    public void getHouseHoldData(){
        try (InputStream inputStream = getClass().getResourceAsStream("/com/lordscave/ranchshopsimulator/data/house_hold.json");
             Reader reader = new InputStreamReader(inputStream)) {

            ObjectMapper mapper = new ObjectMapper();
            HouseHold houseHold = mapper.readValue(reader, HouseHold.class);
            //System.out.println(houseHold.getBalance() + " " + houseHold.getStorage());
            houseHoldBalance = houseHold.getBalance();
            hBalance.setText("Household Balance: $" + formatBalance(houseHold.getBalance()));

        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("$xxxx.xx");
            hBalance.setText("$xxxx.xx");
        }
    }



    public static String formatBalance(int amount) {
        if (amount < 100000) {
            return NumberFormat.getNumberInstance(Locale.US).format(amount);
        } else if (amount >= 1_000_000_000) {
            return String.format("%.2fB", amount / 1_000_000_000.0);
        } else if (amount >= 1_000_000) {
            return String.format("%.2fM", amount / 1_000_000.0);
        } else {
            return String.format("%.2fK", amount / 1_000.0);
        }
    }


    public void setToggleColor(){
        categoryGroup = new ToggleGroup();

        allItem.setToggleGroup(categoryGroup);
        animalItem.setToggleGroup(categoryGroup);
        foodAndWater.setToggleGroup(categoryGroup);
        toolsItem.setToggleGroup(categoryGroup);
        farmingItem.setToggleGroup(categoryGroup);
        gardernItem.setToggleGroup(categoryGroup);
        foodProdItem.setToggleGroup(categoryGroup);

        categoryGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            updateButtonStyles();
        });

        updateButtonStyles();
    }

    private void updateButtonStyles() {
        for (Toggle toggle : categoryGroup.getToggles()) {
            ToggleButton btn = (ToggleButton) toggle;
            if (btn.isSelected()) {
                btn.setStyle("-fx-background-color: #23324F; -fx-text-fill: white;");
            } else {
                btn.setStyle("-fx-background-color: #23324F; -fx-text-fill: grey;");
            }
        }
    }

    public void getAllItem(ActionEvent actionEvent) {
        getShopItems(null);
    }

    public void getAnimalItem(ActionEvent actionEvent) {
        getShopItems("animal");
    }

    public void getFoodAndWaterItem(ActionEvent actionEvent) {
        getShopItems("foodandwater");
    }

    public void getToolsItem(ActionEvent actionEvent) {
        getShopItems("tools");
    }

    public void getFarmingItem(ActionEvent actionEvent) {
        getShopItems("farming");
    }

    public void getGardernItem(ActionEvent actionEvent) {
        getShopItems("gardening");
    }

    public void getFoodProdItem(ActionEvent actionEvent) {
        getShopItems("foodproduction");
    }

    public void getShopItems(String categoryFilter) {
        List<ShopItem> items = ItemLoader.loadItem();
        itemTilePane.getChildren().clear();

        for (ShopItem item : items) {
            if (categoryFilter == null || item.getCategory().equalsIgnoreCase(categoryFilter)) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lordscave/ranchshopsimulator/components/ItemCard.fxml"));
                    Node card = loader.load();

                    ItemCardController controller = loader.getController();
                    controller.setData(item);

                    itemTilePane.getChildren().add(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void updateCartStatic() {
        if (instance != null) {
            instance.updateCartButton();
        }
    }

    public void updateCartButton() {
        int totalQuantity = 0;
        double totalPrice = 0;

        for (CartItem item : CartManager.getInstance().getCartItems()) {
            totalQuantity += item.getQuantity();
            totalPrice += item.getTotalPrice();
        }

        cart.setText("Cart: " + totalQuantity + " $" + String.format("%.2f", totalPrice));
    }

    public void showCart(ActionEvent actionEvent) {
        try {
            List<CartItem> currentCartItems = CartManager.getInstance().getCartItems();
            if (currentCartItems == null || currentCartItems.isEmpty()) {
                messageController.showCustomMessage(MessageType.ERROR, "Your cart is empty!");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lordscave/ranchshopsimulator/CartView.fxml"));
            Parent root = loader.load();

            CartController cartController = loader.getController();

            cartController.setCartData(currentCartItems, houseHoldBalance);
            cartController.setShopController(this);

            Stage cartStage = new Stage();
            cartStage.initStyle(StageStyle.UNDECORATED);
            cartController.setStages(cartStage, (Stage) cart.getScene().getWindow());

            cartStage.setScene(new Scene(root));
            cartStage.show();
            ((Stage) cart.getScene().getWindow()).hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateHouseholdLabel(int balance) {
        hBalance.setText("Household Balance: $" + formatBalance(balance));
        houseHoldBalance = balance;
        cart.setText("Cart: 0 $0.00");

    }

    public void closeShop(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }
}
