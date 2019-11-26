package com.jo.android.smartrestaurant.model;

public class ItemInCart {
    String id;
    String category;
    String itemId;
    int quantity;

    public ItemInCart(String id, String category, String itemId, int quantity) {
        this.id = id;
        this.category = category;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public ItemInCart() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
