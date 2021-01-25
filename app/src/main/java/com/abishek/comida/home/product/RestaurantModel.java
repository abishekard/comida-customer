package com.abishek.comida.home.product;

public class RestaurantModel {
    private String id;
    private String shopName;
    private String shopImage;
    private String speciality;
    private String address;
    private String lat;
    private String lng;
    private String closeTime;
    private String openTime;
    private String available;
    private String rating;

    public RestaurantModel(String id, String shopName, String shopImage,
                           String speciality, String address, String lat,
                           String lng, String closeTime, String openTime,
                           String available, String rating) {
        this.id = id;
        this.shopName = shopName;
        this.shopImage = shopImage;
        this.speciality = speciality;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.closeTime = closeTime;
        this.openTime = openTime;
        this.available = available;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getAvailable() {
        return available;
    }

    public String getRating() {
        return rating;
    }
}
