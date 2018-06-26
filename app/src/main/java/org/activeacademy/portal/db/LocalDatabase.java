package org.activeacademy.portal.db;

import org.activeacademy.portal.models.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class LocalDatabase {

    private static final LocalDatabase ourInstance = new LocalDatabase();

    private final Map<String, List<Item>> inventory = new TreeMap<>();

    private LocalDatabase() {

    }

    public static LocalDatabase getInstance() {
        return ourInstance;
    }

    public Map<String, List<Item>> getInventory() {
        return inventory;
    }

    void sync(HashMap<String, HashMap<String, Long>> items) {
        for (String type : items.keySet()) {
            List<Item> itemList = new ArrayList<>();
            for (String name : items.get(type).keySet()) {
                Long price = items.get(type).get(name);
                itemList.add(new Item(name, price));
            }

            this.inventory.put(type, itemList);
        }
    }

    public CharSequence getType(int position) {
        int i = 0;
        for (String type : inventory.keySet()) {
            if (i++ == position) {
                return type;
            }
        }

        return "";
    }

}
