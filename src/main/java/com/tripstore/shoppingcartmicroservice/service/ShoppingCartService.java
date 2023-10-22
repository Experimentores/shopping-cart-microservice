package com.tripstore.shoppingcartmicroservice.service;

import com.tripstore.shoppingcartmicroservice.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    List<ShoppingCart> getAllShoppingCarts();
    ShoppingCart getShoppingCartById(int id);
    ShoppingCart getShoppingCartByUserId(int id);
    ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);
    void deleteShoppingCart(int id);
    boolean existsByUser_Id(int id);
}