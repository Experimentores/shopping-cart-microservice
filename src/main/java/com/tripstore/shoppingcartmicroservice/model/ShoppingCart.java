package com.tripstore.shoppingcartmicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_created", length = 50)
    private LocalDateTime cartDateCreated;

    @Column(name = "status", length = 50)
    private String cartStatus;

    private int userId;
}
