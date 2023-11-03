package com.tripstore.shoppingcartmicroservice.shoppingcart.domain.services;

import com.crudjpa.service.ICrudService;
import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.model.ShoppingCart;

import java.util.Optional;

public interface IShoppingCartService extends ICrudService<ShoppingCart, Long> {
    Optional<ShoppingCart> deleteShoppingCartByOrderId(Long orderId);
    Optional<ShoppingCart> findShoppingCartByOrderId(Long orderId);
}
