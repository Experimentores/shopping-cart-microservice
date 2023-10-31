package com.tripstore.shoppingcartmicroservice.shoppingcart.mapping;

import com.crudjpa.mapping.IEntityMapper;
import com.tripstore.shoppingcartmicroservice.shared.mapping.EnhancedModelMapper;
import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.model.ShoppingCart;
import com.tripstore.shoppingcartmicroservice.shoppingcart.resources.CreateShoppingCartResource;
import com.tripstore.shoppingcartmicroservice.shoppingcart.resources.ShoppingCartResource;
import com.tripstore.shoppingcartmicroservice.shoppingcart.resources.UpdateShoppingCartResource;
import org.springframework.beans.factory.annotation.Autowired;

public class ShoppingCartMapper implements IEntityMapper<ShoppingCart, ShoppingCartResource, CreateShoppingCartResource, UpdateShoppingCartResource> {
    @Autowired
    EnhancedModelMapper mapper;

    @Override
    public ShoppingCart fromCreateResourceToModel(CreateShoppingCartResource createShoppingCartResource) {
        return mapper.map(createShoppingCartResource, ShoppingCart.class);
    }

    @Override
    public void fromCreateResourceToModel(CreateShoppingCartResource createShoppingCartResource, ShoppingCart shoppingCart) {
        mapper.map(createShoppingCartResource, shoppingCart);
    }

    @Override
    public ShoppingCartResource fromModelToResource(ShoppingCart shoppingCart) {
        return mapper.map(shoppingCart, ShoppingCartResource.class);
    }

    @Override
    public ShoppingCart fromUpdateResourceToModel(UpdateShoppingCartResource updateShoppingCartResource) {
        return mapper.map(updateShoppingCartResource, ShoppingCart.class);
    }

    @Override
    public void fromUpdateResourceToModel(UpdateShoppingCartResource updateShoppingCartResource, ShoppingCart shoppingCart) {
        mapper.map(updateShoppingCartResource, shoppingCart);
    }



}
