package com.firatdemir.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.firatdemir.dto.ProductDTO;
import com.firatdemir.exception.InvalidBarcodeException;
import com.firatdemir.exception.InvalidProductException;
import com.firatdemir.exception.ResourceNotFoundException;
import com.firatdemir.mapper.ProductMapper;
import com.firatdemir.model.PriceComparasion;
import com.firatdemir.model.Product;
import com.firatdemir.repository.PriceComparisonRepository;
import com.firatdemir.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final PriceComparisonRepository priceComparisonRepository;
	private final ProductMapper productMapper;

	@Autowired
	public ProductService(ProductRepository productRepository, PriceComparisonRepository priceComparisonRepository,
			ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.priceComparisonRepository = priceComparisonRepository;
		this.productMapper = productMapper;

	}

	// ürün ekleme
	public Product saveProduct(ProductDTO productDTO) {
		isBarcodeValid(productDTO.getBarcode());
		validatePrice(productDTO.getPrice());
		validateProductName(productDTO.getProductName());

		Product product = productMapper.toEntity(productDTO);
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
		isBarcodeValid(productDTO.getBarcode()); // Barkod kontrolü
		validatePrice(productDTO.getPrice()); // Fiyat kontrolü

		// validateStock(productDTO.getStock()); // Stok kontrolü daha sonra aktif
		// edilecek

		validateProductName(productDTO.getProductName()); // Ürün adı kontrolü

		Product existingProduct = getProductById(id);
		productMapper.updateEntityFromDTO(productDTO, existingProduct);
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

	public void save(Product product) {
		productRepository.save(product); // Spring Data JPA'nın save metodunu kullanarak veritabanına kaydediyoruz
	}

	// Barkodla ürün arama
	public Product findByBarcode(String barcode) {
		return productRepository.findByBarcode(barcode);
	}

	// Barkod ile ürün arar ve ürünü döndürür
	public Product getProductByBarcode(String barcode) {
		isBarcodeValid(barcode); // Geçerli barkod mu kontrol et
		return productRepository.findByBarcode(barcode);
	}

	// Barkodun geçerli olup olmadığını kontrol eder
	private void isBarcodeValid(String barcode) {
		if (barcode == null || !barcode.matches("\\d{12,13}")) {
			throw new InvalidBarcodeException(
					"Geçersiz barkod: Barkod 12 veya 13 haneli olmalı ve sadece sayılardan oluşmalıdır.");
		}
	}

	// Fiyat geçerli mi kontrol eder
	private void validatePrice(Double price) {
		if (price == null || price < 0) {
			throw new InvalidProductException("Geçersiz fiyat: Fiyat negatif olamaz.");
		}
	}

	// Stok geçerli mi kontrol eder
	private void validateStock(Integer stock) {
		if (stock == null || stock < 0) {
			throw new InvalidProductException("Geçersiz stok: Stok miktarı negatif olamaz.");
		}
	}

	// Ürün adı geçerli mi kontrol eder
	private void validateProductName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new InvalidProductException("Geçersiz ürün adı: Ürün adı boş olamaz.");
		}
		if (name.length() > 100) {
			throw new InvalidProductException("Geçersiz ürün adı: Ürün adı 100 karakterden uzun olamaz.");
		}
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

	// Barkod ile fiyat karşılaştırma metodu
	public List<PriceComparasion> getPriceComparisonsByBarcode(String barcode) {
		Product product = productRepository.findByBarcode(barcode);
		if (product == null) {
			throw new ResourceNotFoundException("Barkod ile eşleşen ürün bulunamadı: " + barcode);
		}

		List<PriceComparasion> priceComparisons = priceComparisonRepository.findByProductId(product.getId());
		if (priceComparisons.isEmpty()) {
			throw new ResourceNotFoundException("Bu ürün için fiyat karşılaştırması bulunamadı.");
		}

		return priceComparisons.stream().sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
				.collect(Collectors.toList());
	}

	public void deleteProduct(Long id) {
		Product existingProduct = getProductById(id);
		productRepository.delete(existingProduct);
	}
}
