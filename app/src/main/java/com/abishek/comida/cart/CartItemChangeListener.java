package com.abishek.comida.cart;

public interface CartItemChangeListener {

    void cartQuantityChanged(int quantity,int price,int discount,String IorD);
    void cartItemRemoved(String productId,int quantity,int price,int discount);
    void cartEmpty();

}
