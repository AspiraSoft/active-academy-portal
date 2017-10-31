package com.bytexcite.khaapa.models;


public class Item {

    private final String name;
    private final Long price;

    public Item(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

}
