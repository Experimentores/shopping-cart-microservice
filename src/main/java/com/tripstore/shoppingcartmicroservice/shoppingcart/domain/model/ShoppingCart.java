package com.tripstore.shoppingcartmicroservice.shoppingcart.domain.model;

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
    private Long id;

    @Column(name = "date_created", length = 50)
    private LocalDateTime createdAt;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "order_id", nullable = false)
    private Long orderId;
}
