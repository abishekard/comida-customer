package com.abishek.comida.home.product;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.abishek.comida.cart.cartRoom.TimestampConverter;

import java.util.Date;

@Entity
public class FoodModel {


    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimestampConverter.class})
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


    @Ignore
    public FoodModel(String productId, String foodName, String foodImage,
                     String price, String price_type, String discount,
                     String vegNonVeg, String type, String shopName, String address) {
        this.productId = productId;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.price = price;
        this.price_type = price_type;
        this.discount = discount;
        this.vegNonVeg = vegNonVeg;
        this.type = type;
        this.shopName = shopName;
        this.address = address;

    }


    public FoodModel(String productId, String foodName, String foodImage, String price,
                     String price_type, String discount, String vegNonVeg, String type) {
        this.productId = productId;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.price = price;
        this.price_type = price_type;
        this.discount = discount;
        this.vegNonVeg = vegNonVeg;
        this.type = type;
        this.quantity=1;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
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

    public int getQuantity() {
        return quantity;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
