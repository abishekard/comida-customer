package com.abishek.comida.home.product;

public class FoodModel {
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
}
