package com.tripstore.shoppingcartmicroservice.shoppingcart.controller;

import com.crudjpa.controller.CrudController;
import com.crudjpa.enums.MapFrom;
import com.crudjpa.util.HttpStatusCheckCode;
import com.tripstore.shoppingcartmicroservice.cartitems.client.ICartItemClient;
import com.tripstore.shoppingcartmicroservice.cartitems.domain.model.CartItem;
import com.tripstore.shoppingcartmicroservice.orders.client.IOrderClient;
import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.model.ShoppingCart;
import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.services.IShoppingCartService;
import com.tripstore.shoppingcartmicroservice.shoppingcart.enums.ShoppingCartStatus;
import com.tripstore.shoppingcartmicroservice.shoppingcart.exception.InvalidCreateResourceException;
import com.tripstore.shoppingcartmicroservice.shoppingcart.mapping.ShoppingCartMapper;
import com.tripstore.shoppingcartmicroservice.shoppingcart.resources.CreateShoppingCartResource;
import com.tripstore.shoppingcartmicroservice.shoppingcart.resources.ShoppingCartResource;
import com.tripstore.shoppingcartmicroservice.shoppingcart.resources.UpdateShoppingCartResource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${tripstore.shopping-carts-service.path}")
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
        Optional<?> order = getOrderFromId(createShoppingCartResource.getOrderId());
        if(order.isEmpty())
            throw new InvalidCreateResourceException("Invalid order id or order service is down");

        return true;
    }

    @Override
    protected boolean isValidUpdateResource(UpdateShoppingCartResource updateShoppingCartResource) {
        return true;
    }

    @Override
    protected ShoppingCart fromCreateResourceToModel(CreateShoppingCartResource createShoppingCartResource) {
        ShoppingCart shoppingCart = super.fromCreateResourceToModel(createShoppingCartResource);

        shoppingCart.setCreatedAt(LocalDateTime.now());
        shoppingCart.setStatus(ShoppingCartStatus.IN_PROCESS.name());

        return shoppingCart;
    }

    @Override
    protected ShoppingCartResource fromModelToResource(ShoppingCart shoppingCart, MapFrom from) {
        ShoppingCartResource resource = mapper.fromModelToResource(shoppingCart);
        if(from != MapFrom.ANY) {
            if(from != MapFrom.CREATE) {
                resource.setItems(getCartItemsOfShoppingCart(shoppingCart.getId()));
            }
        }

        return resource;
    }

    private Optional<?> getOrderFromId(Long orderId){
        try {
            ResponseEntity<?> response = orderClient.getOrderById(orderId);
            return HttpStatusCheckCode.from(response).isOk() ? Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private List<CartItem> getCartItemsOfShoppingCart(Long shoppingCaryId) {
        try {
            ResponseEntity<List<CartItem>> response = cartItemClient.getByShoppingCartId(shoppingCaryId);
            return HttpStatusCheckCode.from(response).isOk() ? response.getBody() : List.of();
        } catch (Exception e) {
            return List.of();
        }
    }

    private ShoppingCartResource getShoppingCartOrderResource(ShoppingCart shoppingCart){
        ShoppingCartResource resource = mapper.fromModelToResource(shoppingCart);
        resource.setItems(getCartItemsOfShoppingCart(shoppingCart.getId()));
        return resource;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShoppingCartResource> getShoppingCartById(@PathVariable Long id) {
        return getById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShoppingCartResource>> getAllShoppingCarts() {
        return getAll();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShoppingCartResource> createShoppingCart(@Valid @RequestBody CreateShoppingCartResource createShoppingCartResource, BindingResult result) {
        if(result.hasErrors())
            throw new InvalidCreateResourceException(getErrorsFromResult(result));

        return insert(createShoppingCartResource);
    }

    private void validateCartItemsClient() {
        validateHealthClient(cartItemClient, "CartItems");
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShoppingCartResource> deleteShoppingCartById(@PathVariable Long id) {
        validateCartItemsClient();

        ResponseEntity<ShoppingCartResource> response = delete(id);
        if(HttpStatusCheckCode.from(response).isOk())
            cartItemClient.deleteByShoppingCartId(id);

        return response;
    }

    @GetMapping(value = "orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShoppingCartResource> getShoppingCartByOrderId(@PathVariable Long id) {
        Optional<ShoppingCart> shoppingCart = shoppingCartService.findShoppingCartByOrderId(id);
        return shoppingCart.map(cart -> ResponseEntity.ok(getShoppingCartOrderResource(cart)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(value = "orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<ShoppingCartResource> deleteShoppingCartByOrderId(@PathVariable Long id) {
        validateCartItemsClient();

        Optional<ShoppingCart> shoppingCart = shoppingCartService.deleteShoppingCartByOrderId(id);

        return shoppingCart.map(cart -> ResponseEntity.ok(getShoppingCartOrderResource(cart)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @RequestMapping(value = "healthcheck", method = RequestMethod.HEAD)
    ResponseEntity<Void> isOk() {
        return ResponseEntity.ok().build();
    }
}
