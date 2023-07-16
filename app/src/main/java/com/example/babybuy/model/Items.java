package com.example.babybuy.model;

public class Items {

    String itemId, itemImage, itemName, itemCost, itemDescription, mapsLocation, itemStatus, uid;

    public Items() {
    }

    public Items(String id, String itemId, String itemName, String itemCost, String itemDescription, String mapsLocation, String itemImage) {
        this.uid = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.itemDescription = itemDescription;
        this.mapsLocation = mapsLocation;
        this.itemImage = itemImage;
    }

    public Items(String id, String itemId, String itemName, String itemCost, String itemDescription, String mapsLocation) {
        this.uid = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.itemDescription = itemDescription;
        this.mapsLocation = mapsLocation;
    }

    public Items(String itemId, String itemName, String itemCost, String itemDescription) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.itemDescription = itemDescription;
    }


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCost() {
        return itemCost;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getMapsLocation() {
        return mapsLocation;
    }

    public void setMapsLocation(String mapsLocation) {
        this.mapsLocation = mapsLocation;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}

