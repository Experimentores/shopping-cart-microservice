package com.tripstore.shoppingcartmicroservice.orders.client;

import com.tripstore.shoppingcartmicroservice.orders.domain.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "orders-service")
public interface IOrderClient {
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Order> getOrderById(@PathVariable Long id, @RequestParam(required = false) String getShoppingCart);
}
