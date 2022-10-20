package com.consulteer.shoppingstore.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "price")
    private Double unitPrice;
    @Column(name = "units_in_stock")
    private Integer unitsInStock;
    @JsonManagedReference
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private BasketItem basketItem;

    public Product(String name,
                   String description,
                   Double unitPrice,
                   Integer unitsInStock) {
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
    }
}
