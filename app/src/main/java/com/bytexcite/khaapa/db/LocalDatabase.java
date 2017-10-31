package com.bytexcite.khaapa.db;

import com.bytexcite.khaapa.models.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LocalDatabase {

    private static final LocalDatabase ourInstance = new LocalDatabase();

    private final List<Item> foodItems;
    private final List<Item> drinkItems;
    private final List<Item> otherItems;

    private Item specialItem;

    private LocalDatabase() {
        drinkItems = new ArrayList<>();
        foodItems = new ArrayList<>();
        otherItems = new ArrayList<>();
    }

    public static LocalDatabase getInstance() {
        return ourInstance;
    }

    void syncFoods(HashMap<String, Long> foodItems) {
        this.foodItems.clear();
        for (String name : foodItems.keySet()) {
            Long price = foodItems.get(name);

            Item item = new Item(name, price);
            this.foodItems.add(item);
        }
    }

    void syncDrinks(HashMap<String, Long> drinkItems) {
        this.drinkItems.clear();
        for (String name : drinkItems.keySet()) {
            Long price = drinkItems.get(name);

            Item item = new Item(name, price);
            this.drinkItems.add(item);
        }
    }

    void syncOtherItems(HashMap<String, Long> otherItems) {
        this.otherItems.clear();
        for (String name : otherItems.keySet()) {
            Long price = otherItems.get(name);

            Item item = new Item(name, price);
            this.otherItems.add(item);
        }
    }

    public List<Item> getFoods() {
        return foodItems;
    }

    public List<Item> getDrinks() {
        return drinkItems;
    }

    public List<Item> getOtherItems() {
        return otherItems;
    }

    public Item getSpecialItem() {
        return specialItem;
    }

    public void setSpecialItem(Item specialItem) {
        this.specialItem = specialItem;
    }

}
