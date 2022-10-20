package com.consulteer.shoppingstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "basket")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "total_items_count")
    private Integer totalItemsCount;
    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonManagedReference
    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL)
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<BasketItem> basketItems;

    public Basket(Double totalPrice, Integer totalItemsCount) {
        this.totalPrice = totalPrice;
        this.totalItemsCount = totalItemsCount;
    }
}
