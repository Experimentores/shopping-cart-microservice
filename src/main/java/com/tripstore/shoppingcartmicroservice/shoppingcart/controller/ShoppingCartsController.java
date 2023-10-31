package com.tripstore.shoppingcartmicroservice.shoppingcart.controller;

import com.crudjpa.controller.CrudController;
import com.crudjpa.exception.CreateResourceValidationException;
import com.crudjpa.exception.ResourceNotFoundException;
import com.tripstore.shoppingcartmicroservice.cartitems.client.ICartItemClient;
import com.tripstore.shoppingcartmicroservice.cartitems.domain.model.CartItem;
import com.tripstore.shoppingcartmicroservice.orders.client.IOrderClient;
import com.tripstore.shoppingcartmicroservice.orders.domain.model.Order;
import com.tripstore.shoppingcartmicroservice.shoppingcart.enums.ShoppingCartStatus;
import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.model.ShoppingCart;
import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.services.IShoppingCartService;
import com.tripstore.shoppingcartmicroservice.shoppingcart.mapping.ShoppingCartMapper;
import com.tripstore.shoppingcartmicroservice.shoppingcart.resources.CreateShoppingCartResource;
import com.tripstore.shoppingcartmicroservice.shoppingcart.resources.ShoppingCartResource;
import com.tripstore.shoppingcartmicroservice.shoppingcart.resources.UpdateShoppingCartResource;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/tripstore/v1/shopping-carts")
public class ShoppingCartsController extends CrudController<ShoppingCart, Long, ShoppingCartResource, CreateShoppingCartResource, UpdateShoppingCartResource> {
    private final IShoppingCartService shoppingCartService;
    private final ICartItemClient cartItemClient;
    private final IOrderClient orderClient;
    public ShoppingCartsController(IShoppingCartService shoppingCartService, ShoppingCartMapper mapper, ICartItemClient cartItemClient, IOrderClient orderClient) {
        super(shoppingCartService, mapper);
        this.shoppingCartService = shoppingCartService;
        this.cartItemClient = cartItemClient;
        this.orderClient = orderClient;
        resourceName = "Shopping cart";
    }

    @Override
    protected boolean isValidCreateResource(CreateShoppingCartResource createShoppingCartResource) {
        return true;
    }

    @Override
    protected boolean isValidUpdateResource(UpdateShoppingCartResource updateShoppingCartResource) {
        return true;
    }


    private Optional<Order> getOrderFromId(Long orderId){
        try {
            ResponseEntity<Order> response = orderClient.getOrderById(orderId, "false");
            return response.getStatusCode() == HttpStatus.OK ? Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private List<CartItem> getCartItemsOfShoppingCart(Long shoppingCaryId) {
        try {
            ResponseEntity<List<CartItem>> response = cartItemClient.getByShoppingCartId(shoppingCaryId);
            return response.getStatusCode() == HttpStatus.OK ? response.getBody() : List.of();
        } catch (Exception e) {
            return List.of();
        }
    }

    private ShoppingCart getShoppingCart(Long id) {
        Optional<ShoppingCart> cartItem = shoppingCartService.getById(id);
        if(cartItem.isEmpty())
            throw new ResourceNotFoundException("Cart Item with id: " + id + " not found");
        return cartItem.get();
    }

    private ShoppingCartResource getShoppingCartResource(ShoppingCart shoppingCart, boolean getCartItems){
        ShoppingCartResource resource = mapper.fromModelToResource(shoppingCart);
        Optional<Order> order = getOrderFromId(shoppingCart.getOrderId());
        order.ifPresentOrElse(resource::setOrder, () -> resource.setOrder(null));
        if(getCartItems)
            resource.setItems(getCartItemsOfShoppingCart(shoppingCart.getId()));
        else resource.setItems(List.of());
        return resource;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShoppingCartResource> getById(@PathVariable Long id, @RequestParam(required = false) String getCartItems) {
        ShoppingCart shoppingCart = getShoppingCart(id);
        return ResponseEntity.ok(getShoppingCartResource(shoppingCart, getCartItems == null || getCartItems.equals("true")));
    }

    private List<ShoppingCartResource> mapShoppingCartResources(List<ShoppingCart> shoppingCarts) {
        HashMap<Long, Optional<Order>> orders = new HashMap<>();
        HashMap<Long, List<CartItem>> items = new HashMap<>();

        return shoppingCarts.stream().map(shoppingCart -> {
            ShoppingCartResource resource = fromModelToResource(shoppingCart);
            Optional<Order> order = orders.get(shoppingCart.getOrderId());
            if(order == null) {
                order = getOrderFromId(shoppingCart.getOrderId());
                orders.put(shoppingCart.getOrderId(), order);
            }
            order.ifPresentOrElse(resource::setOrder, () -> resource.setOrder(null));

            List<CartItem> cartItems = items.get(shoppingCart.getId());
            if(cartItems == null) {
                cartItems = getCartItemsOfShoppingCart(shoppingCart.getId());
                items.put(shoppingCart.getId(), cartItems);
            }

            resource.setItems(cartItems);
            return resource;
        }).toList();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShoppingCartResource>> getAllShoppingCarts() {
        List<ShoppingCart> shoppingCarts = shoppingCartService.getAll();
        return ResponseEntity.ok(mapShoppingCartResources(shoppingCarts));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShoppingCartResource> createShoppingCart(@Valid @RequestBody CreateShoppingCartResource createShoppingCartResource, BindingResult result) {
        if(result.hasErrors())
            throw new CreateResourceValidationException(getErrorsFromResult(result));
        Optional<Order> order = getOrderFromId(createShoppingCartResource.getOrderId());
        if(order.isEmpty())
            throw new CreateResourceValidationException("Invalid order id or order service is down");

        ShoppingCart shoppingCart = fromCreateResourceToModel(createShoppingCartResource);
        shoppingCart.setCreatedAt(LocalDateTime.now());
        shoppingCart.setStatus(ShoppingCartStatus.IN_PROCESS.name());

        shoppingCartService.save(shoppingCart);

        ShoppingCartResource resource = fromModelToResource(shoppingCart);
        resource.setOrder(order.get());
        resource.setItems(List.of());

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShoppingCartResource> deleteShoppingCartById(@PathVariable Long id) {
        ShoppingCart shoppingCart = getShoppingCart(id);
        ShoppingCartResource resource = fromModelToResource(shoppingCart);

        Optional<Order> order = getOrderFromId(id);
        order.ifPresentOrElse(resource::setOrder, () -> resource.setOrder(null));

        ResponseEntity<List<CartItem>> response = cartItemClient.deleteByShoppingCartId(id);
        if(response.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.name());

        resource.setItems(response.getBody());
        shoppingCartService.delete(id);
        return ResponseEntity.ok(resource);
    }

}
