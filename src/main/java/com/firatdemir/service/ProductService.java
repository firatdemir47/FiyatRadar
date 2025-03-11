package com.firatdemir.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firatdemir.model.Product;
import com.firatdemir.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	// Ürün ekleme
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	// Ürün listeleme
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	// Ürün bulma
	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	// Ürün silme
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}
