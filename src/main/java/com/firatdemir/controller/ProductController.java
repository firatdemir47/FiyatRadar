package com.firatdemir.controller;

import java.util.List;

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
import com.firatdemir.exception.ResourceNotFoundException;
import com.firatdemir.mapper.ProductMapper;
import com.firatdemir.model.PriceComparasion;
import com.firatdemir.model.Product;
import com.firatdemir.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Ürün İşlemleri", description = "Ürün ekleme, silme, güncelleme ve arama işlemleri")
public class ProductController {

	private final ProductService productService;
	private final ProductMapper productMapper;
	
	
	@Autowired
	public ProductController(ProductService productService, ProductMapper productMapper) {
		this.productService = productService;
		this.productMapper = productMapper;

	}

	

	// ürünün fiyatlarını karşılaştırmak için
	@Operation(summary = "Ürün fiyatlarını karşılaştır", description = "Barkod bilgisi ile ürünlerin fiyatlarını karşılaştırır.")
	@GetMapping("/{barcode}/compare-prices")
	public List<PriceComparasion> getPriceComparisons(@PathVariable String barcode) {
		return productService.getPriceComparisonsByBarcode(barcode);
	}
	
	@Operation(summary = "Yeni ürün ekle", description = "Yeni bir ürün ekler.")
	@PostMapping("/save")
	public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
	    // Ürünü doğrudan DTO olarak gönderiyoruz
	    Product savedProduct = productService.saveProduct(productDTO);

	    // DTO'ya çevirip döndür
	    ProductDTO productDTOResponse = productMapper.toDTO(savedProduct);
	    return new ResponseEntity<>(productDTOResponse, HttpStatus.CREATED);
	}


	

	// Ürünleri listeleme
	@Operation(summary = "Tüm ürünleri getir", description = "Veritabanındaki tüm ürünleri listeler")
	@GetMapping
	public List<ProductDTO> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		return products.stream().map(productMapper::toDTO).collect(Collectors.toList()); // Stream kullanarak dönüşüm
	}

	// ID ile ürün getirme
	@Operation(summary = "ID ile ürün getir", description = "Verilen ID ile bir ürünü getirir.")
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
		Product product = productService.getProductById(id);
		return ResponseEntity.ok(productMapper.toDTO(product)); // DTO'ya dönüştürüp döndür
	}

	// Ürün güncelleme
	@Operation(summary = "Ürünü güncelle", description = "Verilen ID ile bir ürünü günceller.")
	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
		Product updatedProduct = productService.updateProduct(id, productDTO);
		return ResponseEntity.ok(productMapper.toDTO(updatedProduct)); // DTO'ya dönüştürüp döndür
	}

	// Ürün silme
	@Operation(summary = "Ürünü sil", description = "Verilen ID ile bir ürünü siler.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	// Barkod ile ürün arama
	@Operation(summary = "Barkod ile ürün arama", description = "Barkod bilgisi ile bir ürünü arar.")
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

	@GetMapping("/barcode/{barcode}")
	@Operation(summary = "Barkod ile ürün getirme", description = "Bu operasyon, verilen barkod ile ürünü arar ve detaylarını döndürür.")
	public ResponseEntity<Product> getProductByBarcode(@PathVariable String barcode) {
		Product product = productService.getProductByBarcode(barcode);
		if (product == null) {
			throw new ResourceNotFoundException("Bu barkoda sahip ürün bulunamadı: " + barcode);
		}
		return ResponseEntity.ok(product);
	}

	// barcode fiyatını güncelleyen endpoint
	@Operation(summary = "Barkod fiyatını güncelle", description = "Barkod bilgisi ile ürünün fiyatını günceller.")
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
	@Operation(summary = "Barkodu güncelle", description = "Eski barkod bilgisi ile yeni barkod bilgisini değiştirir.")
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
	@Operation(summary = "Ürün arama", description = "Ürünleri isme ve kategoriye göre arar.")
	@GetMapping("/search")
	public List<ProductDTO> searchProducts(@RequestParam(required = false) String name,
			@RequestParam(required = false) String category) {
		List<Product> products = productService.searchProducts(name, category);
		return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
	}

	// Ürün filtreleme
	@Operation(summary = "Ürün filtreleme", description = "Fiyat aralığı ve kategoriye göre ürünleri filtreler.")
	@GetMapping("/filter")
	public List<ProductDTO> filterProducts(@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice, @RequestParam(required = false) String category) {
		List<Product> products = productService.filterProducts(minPrice, maxPrice, category);
		return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
	}

}
