package com.tripstore.shoppingcartmicroservice.orders.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="${tripstore.orders-service.name}",
        path = "${tripstore.orders-service.path}")
public interface IOrderClient {
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getOrderById(@PathVariable Long id);
}
