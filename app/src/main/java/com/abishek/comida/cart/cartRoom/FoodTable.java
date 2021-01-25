package com.abishek.comida.cart.cartRoom;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

@Entity
public class FoodTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private String id;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "modified_at")
    private Date modifiedAt;


    private String productId;
    private String foodName;
    private String foodImage;
    private String price;
    private String price_type;
    private String discount;
    private String vegNonVeg;
    private String type;
    private String shopName;
    private String address;
    private int quantity;


    public FoodTable(String productId, String foodName, String foodImage,
                     String price, String price_type, String discount, String vegNonVeg,
                     String type) {
        this.productId = productId;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.price = price;
        this.price_type = price_type;
        this.discount = discount;
        this.vegNonVeg = vegNonVeg;
        this.type = type;

    }



    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public String getProductId() {
        return productId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public String getPrice() {
        return price;
    }

    public String getPrice_type() {
        return price_type;
    }

    public String getDiscount() {
        return discount;
    }

    public String getVegNonVeg() {
        return vegNonVeg;
    }

    public String getType() {
        return type;
    }

    public String getShopName() {
        return shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setVegNonVeg(String vegNonVeg) {
        this.vegNonVeg = vegNonVeg;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
