package com.tripstore.shoppingcartmicroservice.shoppingcart.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateShoppingCartResource {
    @NotNull
    @Positive()
    private Long orderId;
}
