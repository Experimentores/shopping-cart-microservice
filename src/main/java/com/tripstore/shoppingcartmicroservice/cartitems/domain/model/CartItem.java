package com.tripstore.shoppingcartmicroservice.cartitems.domain.model;

import com.tripstore.shoppingcartmicroservice.products.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Long id;
    private int quantity;
    private double subtotal;
    private Product product;
}
