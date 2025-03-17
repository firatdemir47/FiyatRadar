package com.firatdemir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firatdemir.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByBarcode(String barcode);
}
