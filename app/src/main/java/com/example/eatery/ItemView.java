package com.example.eatery;

public class ItemView {
    private int itemImageId;

    // TextView 1
    private String itemName;

    // TextView 1
    private int itemPrice;

    // create constructor to set the values for all the parameters of the each single view
    public ItemView(int ItemImageId, String ItemName, int ItemPrice) {
        itemImageId = ItemImageId;
        itemName = ItemName;
        itemPrice = ItemPrice;
    }

    // getter method for returning the ID of the imageview
    public int ItemImageId() {
        return itemImageId;
    }

    // getter method for returning the ID of the TextView 1
    public String getItemName() {
        return itemName;
    }

    // getter method for returning the ID of the TextView 2
    public int getItemPrice() {
        return itemPrice;
    }
}

