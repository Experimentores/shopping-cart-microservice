package com.tripstore.shoppingcartmicroservice.shoppingcart.persistence.repository;

import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> deleteShoppingCartsByOrderId(Long orderId);
    List<ShoppingCart> findShoppingCartsByOrderId(Long orderId);
}