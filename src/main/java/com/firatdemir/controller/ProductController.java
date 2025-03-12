package com.firatdemir.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

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
	    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
	        Product createProduct = productService.saveProduct(product);
	        return new ResponseEntity<>(createProduct, HttpStatus.CREATED);
	    }

	    // Ürünleri listeleme
	    @GetMapping
	    public List<Product> getAllProducts() {
	        return productService.getAllProducts();
	    }

	    // ID ile ürün getirme
	    @GetMapping("/{id}")
	    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
	        Product product = productService.getProductById(id); // Optional yerine direkt Product döndürüyoruz
	        return ResponseEntity.ok(product);
	    }

	    // Ürün güncelleme
	    @PutMapping("/{id}")
	    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
	        Product updatedProduct = productService.updateProduct(id, product);
	        return ResponseEntity.ok(updatedProduct);
	    }

	    // Ürün silme
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
	        productService.deleteProduct(id);
	        return ResponseEntity.noContent().build();
	    }

}
