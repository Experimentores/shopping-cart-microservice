package com.tripstore.shoppingcartmicroservice.cartitems.client;

import com.tripstore.shoppingcartmicroservice.cartitems.domain.model.CartItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="${tripstore.cart-items-service.name}",
        path = "${tripstore.cart-items-service.path}")
public interface ICartItemClient {

    @GetMapping(value = "shopping-carts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CartItem>> getByShoppingCartId(@PathVariable Long id);

    @DeleteMapping(value = "shopping-carts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CartItem>> deleteByShoppingCartId(@PathVariable Long id);
}
