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
import com.firatdemir.model.Product;
import com.firatdemir.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	// Ürün ekleme

	@PostMapping("/save")
	public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
		Product product = productService.saveProduct(productDTO);
		return new ResponseEntity<>(convertToDTO(product), HttpStatus.CREATED);
	}

	// Ürünleri listeleme

	@GetMapping
	public List<ProductDTO> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		return products.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// ID ile ürün getirme
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
		Product product = productService.getProductById(id);
		return ResponseEntity.ok(convertToDTO(product));
	}

	// Ürün güncelleme
	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
		Product updatedProduct = productService.updateProduct(id, productDTO);
		return ResponseEntity.ok(convertToDTO(updatedProduct));
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
			ProductDTO productDTO = convertToDTO(product);
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
		return products.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// Ürün filtreleme
	@GetMapping("/filter")
	public List<ProductDTO> filterProducts(@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice, @RequestParam(required = false) String category) {
		List<Product> products = productService.filterProducts(minPrice, maxPrice, category);
		return products.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private ProductDTO convertToDTO(Product product) {
		return new ProductDTO(product.getName(), // Entity'deki name, DTO'da productName olarak yer alır.
				product.getBarcode(), product.getDescription(), product.getPrice(), product.getStoreName());
	}

}
