package com.tripstore.shoppingcartmicroservice.shoppingcart.persistence.repository;

import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> deleteShoppingCartByOrderId(Long orderId);
    Optional<ShoppingCart> findShoppingCartByOrderId(Long orderId);
}