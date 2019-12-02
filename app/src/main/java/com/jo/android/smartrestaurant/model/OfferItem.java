package com.jo.android.smartrestaurant.model;

public class OfferItem {
    private String name;
    private String description;
    private long price;
    private String state;

    public OfferItem(String name, String description, long price, String state) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = state;
    }

    public OfferItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
