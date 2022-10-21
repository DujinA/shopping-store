package com.consulteer.shoppingstore.repositories;

import com.consulteer.shoppingstore.domain.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

    @Modifying
    @Query( value = "delete from basket_item where product_id = :id", nativeQuery = true)
    void deleteByProductId(@Param("id") Long productId);

    @Modifying
    @Query(value = "delete from basket_item where basket_id = :id", nativeQuery = true)
    void deleteAllByBasketId(@Param("id") Long basketId);
}
