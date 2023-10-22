package com.tripstore.shoppingcartmicroservice.controller;

import com.tripstore.shoppingcartmicroservice.exception.ResourceNotFoundException;
import com.tripstore.shoppingcartmicroservice.exception.ValidationException;
import com.tripstore.shoppingcartmicroservice.model.ShoppingCart;
import com.tripstore.shoppingcartmicroservice.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tripstore/v1")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    //private final UserService userService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    // Endpoint: /api/tripstore/v1/shopping-carts
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/shopping-carts")
    public ResponseEntity<List<ShoppingCart>> getAllShoppingCarts() {
        return new ResponseEntity<>(shoppingCartService.getAllShoppingCarts(), HttpStatus.OK);
    }

    // Endpoint: /api/tripstore/v1/shopping-carts/{id}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/shopping-carts/{id}")
    public ResponseEntity<ShoppingCart> getShoppingCartById(@PathVariable int id) {
        return new ResponseEntity<>(shoppingCartService.getShoppingCartById(id), HttpStatus.OK);
    }

    // Endpoint: /api/tripstore/v1/shopping-carts/users/{id}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/shopping-carts/users/{id}")
    public ResponseEntity<ShoppingCart> getShoppingCartByUserId(@PathVariable int id) {
        //notFoundUser(id);
        notFoundShoppingCartByUserId(id);
        return new ResponseEntity<>(shoppingCartService.getShoppingCartByUserId(id), HttpStatus.OK);
    }

    // Endpoint: /api/tripstore/v1/shopping-carts
    // Method: POST
    @Transactional
    @PostMapping("/shopping-carts")
    public ResponseEntity<ShoppingCart> createShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        //validateShoppingCart(shoppingCart);
        //notFoundUser(shoppingCart.getUser().getId());
        //existsShoppingCartByUserId(shoppingCart.getUser().getId());
        return new ResponseEntity<>(shoppingCartService.saveShoppingCart(shoppingCart), HttpStatus.CREATED);
    }

    // Endpoint: /api/tripstore/v1/shopping-carts/{id}
    // Method: DELETE
    @DeleteMapping("/shopping-carts/{id}")
    public ResponseEntity<String> deleteShoppingCart(@PathVariable int id) {
        shoppingCartService.deleteShoppingCart(id);
        return new ResponseEntity<>("ShoppingCart with id: " + id + " was deleted", HttpStatus.OK);
    }

    /*public void validateShoppingCart(ShoppingCart shoppingCart) {
        if (shoppingCart.getUser() == null) {
            throw new ValidationException("User is required");
        }
    }*/

    private void existsShoppingCartByUserId(int id) {
        if (shoppingCartService.existsByUser_Id(id)) {
            throw new ValidationException("ShoppingCart with user id: " + id + " already exists");
        }
    }

    /*private void notFoundUser(int id) {
        if (userService.getUserById(id) == null) {
            throw new ResourceNotFoundException("User with id: " + id + " not found");
        }
    }*/

    private void notFoundShoppingCartByUserId(int id) {
        if (shoppingCartService.getShoppingCartByUserId(id) == null) {
            throw new ResourceNotFoundException("ShoppingCart with user id: " + id + " not found");
        }
    }
}
