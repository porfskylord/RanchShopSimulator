package com.lordscave.ranchshopsimulator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopItem {

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private int price;

    @JsonProperty("imagePath")
    private String imagePath;

    @JsonProperty("category")
    private String category;

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }
}
