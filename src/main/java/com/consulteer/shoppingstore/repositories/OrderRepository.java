package com.consulteer.shoppingstore.repositories;

import com.consulteer.shoppingstore.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserIdOrderByCreatedAtDesc(Long userId,
                                                    Pageable pageable);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.user.id = :userId " +
            "AND o.createdAt > COALESCE(:from, '1900-01-01') " +
            "AND o.createdAt < COALESCE(:to, NOW()) " +
            "ORDER BY o.createdAt DESC")
    Page<Order> findAllByUserIdAndTimeInterval(Long userId,
                                               Pageable pageable,
                                               Date from,
                                               Date to);

    Page<Order> findAll(Pageable pageable);
}
