package com.jo.android.smartrestaurant.model;

public class OrderItem {
    private String itemId, category;
    private long quantity;

    public OrderItem(String name, String category, long quantity) {
        this.itemId = name;
        this.category = category;
        this.quantity = quantity;
    }

    public OrderItem() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
