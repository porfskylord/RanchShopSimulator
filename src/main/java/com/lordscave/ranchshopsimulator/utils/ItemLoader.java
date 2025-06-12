package com.lordscave.ranchshopsimulator.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lordscave.ranchshopsimulator.model.ShopItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ItemLoader {
    public static List<ShopItem> loadItem() {
        try{
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = ItemLoader.class.getResourceAsStream("/com/lordscave/ranchshopsimulator/data/shop_item.json");
            return mapper.readValue(inputStream, new TypeReference<List<ShopItem>>() {});
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
