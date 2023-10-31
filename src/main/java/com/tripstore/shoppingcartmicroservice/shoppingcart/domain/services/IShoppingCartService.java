package com.tripstore.shoppingcartmicroservice.shoppingcart.domain.services;

import com.crudjpa.service.ICrudService;
import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.model.ShoppingCart;

import java.util.List;

public interface IShoppingCartService extends ICrudService<ShoppingCart, Long> {
    List<ShoppingCart> deleteShoppingCartsByOrderId(Long orderId);
    List<ShoppingCart> findShoppingCartsByOrderId(Long orderId);
}
