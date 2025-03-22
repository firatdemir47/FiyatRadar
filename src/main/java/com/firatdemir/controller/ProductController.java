package com.firatdemir.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.firatdemir.dto.ProductDTO;
import com.firatdemir.mapper.ProductMapper;
import com.firatdemir.model.PriceComparasion;
import com.firatdemir.model.Product;
import com.firatdemir.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;
	private final ProductMapper productMapper;

	@Autowired
	public ProductController(ProductService productService, ProductMapper productMapper) {
		this.productService = productService;
		this.productMapper = productMapper;
	}
	//ürünün fiyatlarını karşılaştırmak için 
	@GetMapping("/{barcode}/compare-prices")
	public List<PriceComparasion> getPriceComparisons(@PathVariable String barcode) {
		return productService.getPriceComparisonsByBarcode(barcode);
	}

	// Ürün ekleme
	@PostMapping("/save")
	public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
		Product product = productService.saveProduct(productDTO);
		return new ResponseEntity<>(productMapper.toDTO(product), HttpStatus.CREATED); // DTO'ya dönüştürüp döndür
	}

	// Ürünleri listeleme
	@GetMapping
	public List<ProductDTO> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		return products.stream().map(productMapper::toDTO).collect(Collectors.toList()); // Stream kullanarak dönüşüm
	}

	// ID ile ürün getirme
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
		Product product = productService.getProductById(id);
		return ResponseEntity.ok(productMapper.toDTO(product)); // DTO'ya dönüştürüp döndür
	}

	// Ürün güncelleme
	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
		Product updatedProduct = productService.updateProduct(id, productDTO);
		return ResponseEntity.ok(productMapper.toDTO(updatedProduct)); // DTO'ya dönüştürüp döndür
	}

	// Ürün silme
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	// Barkod ile ürün arama
	@GetMapping("/searchByBarcode")
	public ResponseEntity<ProductDTO> searchByBarcode(@RequestParam String barcode) {
		Product product = productService.findByBarcode(barcode);
		if (product != null) {
			ProductDTO productDTO = productMapper.toDTO(product); // DTO'ya dönüştürüp döndür
			return ResponseEntity.ok(productDTO);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// barcode fiyatını güncelleyen endpoint
	@PutMapping("/updatePrice")
	public ResponseEntity<ProductDTO> updatePrice(@RequestParam String barcode, @RequestParam double newPrice) {
		Product product = productService.findByBarcode(barcode);
		if (product != null) {
			product.setPrice(newPrice);
			productService.save(product);
			ProductDTO productDTO = productMapper.toDTO(product);
			return ResponseEntity.ok(productDTO);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// Eski barkodu alıp, yeni barkod ile günceller
	@PutMapping("/updateBarcode")
	public ResponseEntity<ProductDTO> updateBarcode(@RequestParam String oldBarcode, @RequestParam String newBarcode) {
		Product product = productService.findByBarcode(oldBarcode);
		if (product != null) {
			product.setBarcode(newBarcode);
			productService.save(product);
			ProductDTO productDTO = productMapper.toDTO(product);
			return ResponseEntity.ok(productDTO);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// Ürün arama
	@GetMapping("/search")
	public List<ProductDTO> searchProducts(@RequestParam(required = false) String name,
			@RequestParam(required = false) String category) {
		List<Product> products = productService.searchProducts(name, category);
		return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
	}

	// Ürün filtreleme
	@GetMapping("/filter")
	public List<ProductDTO> filterProducts(@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice, @RequestParam(required = false) String category) {
		List<Product> products = productService.filterProducts(minPrice, maxPrice, category);
		return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
	}

}
