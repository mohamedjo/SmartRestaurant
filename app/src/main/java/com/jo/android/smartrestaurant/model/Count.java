package com.jo.android.smartrestaurant.model;

public class Count implements Comparable<Count> {
    private String Category,itemId;
    private long count;

    public Count(String category, String itemId, long count) {
        Category = category;
        this.itemId = itemId;
        this.count = count;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }






    @Override
    public int compareTo(Count count) {
        return Long.compare(this.count,count.count);
    }
}
