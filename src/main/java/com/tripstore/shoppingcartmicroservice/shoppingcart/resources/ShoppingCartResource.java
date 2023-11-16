package com.tripstore.shoppingcartmicroservice.shoppingcart.resources;

import com.tripstore.shoppingcartmicroservice.cartitems.domain.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartResource {
    private Long id;
    private LocalDateTime createdAt;
    private String status;
    private List<CartItem> items;
    private Long orderId;
}
