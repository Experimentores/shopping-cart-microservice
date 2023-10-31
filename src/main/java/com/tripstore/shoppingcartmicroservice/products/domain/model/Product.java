package com.tripstore.shoppingcartmicroservice.products.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String name;

    private String description;

    private Double price;

    private String imageUrl;

    private Double rating;

    private String category;
}
