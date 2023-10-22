package com.tripstore.shoppingcartmicroservice.repository;

import com.tripstore.shoppingcartmicroservice.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    boolean existsByUser_Id(int id);
    ShoppingCart findByUser_Id(int id);
}