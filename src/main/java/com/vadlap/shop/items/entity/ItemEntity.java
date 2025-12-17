package com.vadlap.shop.items.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Table(name="items")
public class ItemEntity implements IItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @DecimalMin(value = "0.00", inclusive = true)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false)
    private Long shopId;

    @Min(0)
    private short itemCounter;

    @Column(nullable = false, unique = true)
    private Long article;

    private String imageUrl;

    @Lob
    @Column(nullable = false)
    private String description;

    private String brand;
    private int weight;
}
