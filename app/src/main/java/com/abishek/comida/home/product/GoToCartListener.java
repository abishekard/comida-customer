package com.abishek.comida.home.product;

public interface GoToCartListener {
    void itemAdded(int price);
    void increased(int price);
    void decreased(int price);
    void cartClear();
}
