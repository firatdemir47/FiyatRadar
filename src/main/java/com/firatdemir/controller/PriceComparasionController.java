package com.firatdemir.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firatdemir.dto.PriceComparisonDTO;
import com.firatdemir.model.PriceComparasion;
import com.firatdemir.model.Product;
import com.firatdemir.repository.ProductRepository;
import com.firatdemir.service.PriceComparisonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/price-comparisons")
@Tag(name = "Fiyat Karşılaştırmaları", description = "Ürün fiyatlarının karşılaştırıldığı ve kaydedildiği işlemler")
public class PriceComparasionController {

	private final PriceComparisonService priceComparisonService;
	private final ProductRepository productRepository; // ProductRepository'yi de inject ediyoruz

	@Autowired
	public PriceComparasionController(PriceComparisonService priceComparisonService,
			ProductRepository productRepository) {
		this.priceComparisonService = priceComparisonService;
		this.productRepository = productRepository;
	}

	@PostMapping("/update-all")
	public ResponseEntity<String> updateAllPrices() {
		try {
			priceComparisonService.updateAllPriceComparisons();
			return ResponseEntity.ok("Tüm ürünlerin fiyat karşılaştırmaları güncellendi.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Fiyat karşılaştırmaları güncellenirken bir hata oluştu: " + e.getMessage());
		}
	}

	/*
	 * @PostMapping("/update") public ResponseEntity<String> updatePrices() { try {
	 * priceComparisonService.updateSinglePriceComparison(); return
	 * ResponseEntity.ok("Fiyat karşılaştırmaları güncellendi."); } catch (Exception
	 * e) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Fiyat karşılaştırmaları güncellenirken bir hata oluştu: " +
	 * e.getMessage()); } }
	 */
	// fiyat karşılaştırma ekleme
	@Operation(summary = "Yeni fiyat karşılaştırması ekle", description = "Yeni fiyat karşılaştırması ekler. Eğer ürün yoksa, yeni ürün oluşturur.")
	@PostMapping
	public ResponseEntity<PriceComparasion> createPriceComparison(@RequestBody PriceComparisonDTO priceComparisonDTO) {
		// PriceComparisonDTO'dan barcode alınıyor
		Product product = productRepository.findByBarcode(priceComparisonDTO.getProductBarcode());

		// Eğer product null ise, yeni product oluşturup kaydetmek
		if (product == null) {
			// Burada yeni bir ürün oluşturuluyor
			product = new Product();
			product.setBarcode(priceComparisonDTO.getProductBarcode());
			product.setPrice(priceComparisonDTO.getPrice());
			product.setStoreName(priceComparisonDTO.getStoreName());
			product.setName("Default Product"); // Burada istersek name'i almak da gerekebilir
			productRepository.save(product); // Ürünü veritabanına kaydediyoruz
		}

		// PriceComparasion oluşturuluyor
		PriceComparasion priceComparasion = new PriceComparasion();
		priceComparasion.setProduct(product); // Ürünü PriceComparasion'a ilişkilendiriyoruz
		priceComparasion.setPrice(priceComparisonDTO.getPrice());
		priceComparasion.setStoreName(priceComparisonDTO.getStoreName());

		// PriceComparasion kaydediliyor
		PriceComparasion createdPriceComparasion = priceComparisonService.savePriceComparasion(priceComparasion);

		return new ResponseEntity<>(createdPriceComparasion, HttpStatus.CREATED);
	}

	// tüm fiyat karşılaştırmaları listeleme
	@Operation(summary = "Tüm fiyat karşılaştırmalarını listele", description = "Veritabanındaki tüm fiyat karşılaştırmalarını listeler.")
	@GetMapping
	public List<PriceComparisonDTO> getAllPriceComparisons() {
		List<PriceComparasion> priceComparasions = priceComparisonService.getAllPriceComparasions();
		return priceComparasions.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// PriceComparisonDTO'dan PriceComparasion entity'sine dönüştürme
	private PriceComparasion convertToEntity(PriceComparisonDTO priceComparisonDTO) {
		Product product = new Product(); // Barcode'a göre Product'ı getirebiliriz
		product.setBarcode(priceComparisonDTO.getProductBarcode()); // Bu kısmı, ilgili barcode'a göre ürünü almak için
																	// geliştirebiliriz
		PriceComparasion priceComparasion = new PriceComparasion();
		priceComparasion.setProduct(product);
		priceComparasion.setPrice(priceComparisonDTO.getPrice());
		priceComparasion.setStoreName(priceComparisonDTO.getStoreName());
		return priceComparasion;
	}

	// PriceComparasion entity'sinden PriceComparisonDTO'ya dönüştürme
	private PriceComparisonDTO convertToDTO(PriceComparasion priceComparasion) {
		// Eğer product null ise, uygun bir hata işlemi yapabiliriz
		if (priceComparasion.getProduct() == null) {
			// Örneğin, hata mesajı verebiliriz veya null değerleri döndürebiliriz.
			throw new IllegalArgumentException("Product is null for PriceComparasion ID: " + priceComparasion.getId());
		}

		// Product null değilse, DTO'yu dolduruyoruz
		PriceComparisonDTO priceComparisonDTO = new PriceComparisonDTO(priceComparasion.getProduct().getBarcode(),
				priceComparasion.getPrice(), priceComparasion.getStoreName());
		return priceComparisonDTO;
	}

}
