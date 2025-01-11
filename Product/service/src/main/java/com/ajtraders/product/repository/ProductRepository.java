package com.ajtraders.product.repository;

import com.ajtraders.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "SELECT * FROM products p ORDER BY p.created_at DESC LIMIT 4", nativeQuery = true)
    List<Product> findMostRecentProducts();

}
