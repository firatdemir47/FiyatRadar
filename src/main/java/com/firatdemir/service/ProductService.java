package com.firatdemir.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firatdemir.dto.ProductDTO;
import com.firatdemir.exception.ResourceNotFoundException;
import com.firatdemir.model.Product;
import com.firatdemir.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	// Ürün ekleme
	public Product saveProduct(ProductDTO productDTO) {
		Product product = new Product();
		product.setName(productDTO.getProductName()); // `productName` yerine `name`
		product.setBarcode(productDTO.getBarcode());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setStoreName(productDTO.getStoreName());
		return productRepository.save(product);
	}

	// Tüm ürünleri listeleme
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	// ID'ye göre ürün bulma
	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

	// Ürün güncelleme

	public Product updateProduct(Long id, ProductDTO productDTO) {
		Product existingProduct = getProductById(id);
		existingProduct.setName(productDTO.getProductName());
		existingProduct.setBarcode(productDTO.getBarcode());
		existingProduct.setDescription(productDTO.getDescription());
		existingProduct.setPrice(productDTO.getPrice());
		existingProduct.setStoreName(productDTO.getStoreName());
		return productRepository.save(existingProduct);
	}

	// Ürün arama
	public List<Product> searchProducts(String name, String category) {
		if (name != null && category != null) {
			return productRepository.findByNameContainingAndCategoryContaining(name, category);
		} else if (name != null) {
			return productRepository.findByNameContaining(name);
		} else if (category != null) {
			return productRepository.findByCategoryContaining(category);
		} else {
			return productRepository.findAll(); // Eğer hiç filtreleme yapılmazsa, tüm ürünleri getir
		}
	}

	// Barkodla ürün arama
	public Product findByBarcode(String barcode) {
		return productRepository.findByBarcode(barcode);
	}

	// Ürün filtreleme
	public List<Product> filterProducts(Double minPrice, Double maxPrice, String category) {
		if (minPrice != null && maxPrice != null && category != null) {
			return productRepository.findByPriceBetweenAndCategory(minPrice, maxPrice, category);
		} else if (minPrice != null && maxPrice != null) {
			return productRepository.findByPriceBetween(minPrice, maxPrice);
		} else if (category != null) {
			return productRepository.findByCategoryContaining(category);
		} else {
			return productRepository.findAll(); // Eğer hiç filtreleme yapılmazsa, tüm ürünleri getir
		}
	}

	public void deleteProduct(Long id) {
		Product existingProduct = getProductById(id);
		productRepository.delete(existingProduct);
	}
}
