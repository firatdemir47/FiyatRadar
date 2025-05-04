package com.firatdemir.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firatdemir.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsByBarcode(String barcode);

	Product findByBarcode(String barcode);

	List<Product> findByNameContaining(String name); // Ürün adıyla arama

	List<Product> findByCategoryContaining(String category); // Kategoriyle arama

	List<Product> findByPriceBetween(Double minPrice, Double maxPrice); // Fiyat aralığıyla arama

	List<Product> findByNameContainingAndCategoryContaining(String name, String category); // Ürün adı ve kategoriyle
																							// arama

	List<Product> findByPriceBetweenAndCategory(Double minPrice, Double maxPrice, String category); // Fiyat ve
																									// kategoriyle arama

}
