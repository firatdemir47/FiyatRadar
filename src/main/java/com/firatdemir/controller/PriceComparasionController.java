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

@RestController
@RequestMapping("/api/price-comparisons")
public class PriceComparasionController {

	private final PriceComparisonService priceComparisonService;
    private final ProductRepository productRepository;  // ProductRepository'yi de inject ediyoruz

    @Autowired
    public PriceComparasionController(PriceComparisonService priceComparisonService, ProductRepository productRepository) {
        this.priceComparisonService = priceComparisonService;
        this.productRepository = productRepository;
    }

	// fiyat karşılaştırma ekleme
	
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
            product.setName("Default Product");  // Burada istersek name'i almak da gerekebilir
            productRepository.save(product);  // Ürünü veritabanına kaydediyoruz
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
	// tüm fiyat karşılaştırmaları ekleme
	
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
