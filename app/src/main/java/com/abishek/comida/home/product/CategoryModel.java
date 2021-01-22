package com.abishek.comida.home.product;

import java.util.ArrayList;

public class CategoryModel {

   /* private String partnerId;
    private String shopName;
    private String shopImage;
    private String shopRating;
    private String openTime;
    private String closeTime;
    private String address;*/
    private String categoryName;
    private ArrayList<FoodModel> foodList;


    public CategoryModel(String categoryName, ArrayList<FoodModel> foodList) {
        this.categoryName = categoryName;
        this.foodList = foodList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public ArrayList<FoodModel> getFoodList() {
        return foodList;
    }
}
