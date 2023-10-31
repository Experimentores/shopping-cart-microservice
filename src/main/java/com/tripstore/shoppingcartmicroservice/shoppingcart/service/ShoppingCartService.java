package com.tripstore.shoppingcartmicroservice.shoppingcart.service;

import com.crudjpa.service.impl.CrudService;
import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.model.ShoppingCart;
import com.tripstore.shoppingcartmicroservice.shoppingcart.domain.services.IShoppingCartService;
import com.tripstore.shoppingcartmicroservice.shoppingcart.persistence.repository.IShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService extends CrudService<ShoppingCart, Long> implements IShoppingCartService {
    private final IShoppingCartRepository shoppingCartRepository;
    public ShoppingCartService(IShoppingCartRepository shoppingCartRepository) {
        super(shoppingCartRepository);
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public List<ShoppingCart> deleteShoppingCartsByOrderId(Long orderId) {
        return shoppingCartRepository.deleteShoppingCartsByOrderId(orderId);
    }

    @Override
    public List<ShoppingCart> findShoppingCartsByOrderId(Long orderId) {
        return shoppingCartRepository.findShoppingCartsByOrderId(orderId);
    }
}
