package com.consulteer.shoppingstore.repositories;

import com.consulteer.shoppingstore.domain.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

    @Query( value = "delete from basket_item where product_id = :id", nativeQuery = true)
    void deleteByProductId(@Param("id") Long productId);
}
