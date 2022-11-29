package com.consulteer.shoppingstore.repositories;

import com.consulteer.shoppingstore.domain.Product;
import com.consulteer.shoppingstore.dtos.interfaces.CityReport;
import com.consulteer.shoppingstore.dtos.interfaces.ProductReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * " +
            "FROM product p " +
            "WHERE created_at BETWEEN DATE_SUB(now(),INTERVAL 1 WEEK) AND NOW()" +
            "ORDER BY created_at DESC " +
            "LIMIT 5 ",
            nativeQuery = true)
    List<Product> findNewProducts();

    @Query(value = "SELECT p.* " +
            "FROM order_item oi " +
            "JOIN orders o ON (oi.order_id = o.id) " +
            "JOIN product p ON (p.id = oi.product_id) " +
            "WHERE o.created_at BETWEEN DATE_SUB(now(),INTERVAL 2 WEEK) AND NOW() " +
            "GROUP BY oi.product_id " +
            "ORDER BY sum(oi.quantity) desc " +
            "LIMIT 10",
            nativeQuery = true)
    List<Product> findTopProducts();

    @Query(value = "SELECT SUM(oi.quantity) as soldQuantity, o.city as cityName " +
            "From orders o " +
            "join order_item oi ON (o.id = oi.order_id) " +
            "WHERE oi.product_id = :productId " +
            "GROUP BY o.city",
            nativeQuery = true)
    List<CityReport> findCitiesInfo(Long productId);

    @Query(value = "SELECT p.name as productName, p.price as productPrice " +
            "FROM product p " +
            "WHERE p.id = :productId",
            nativeQuery = true)
    Optional<ProductReport> findProductNameAndPrice(Long productId);
}
