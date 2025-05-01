package com.firatdemir.FiyatRadar;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.firatdemir.dto.ProductDTO;
import com.firatdemir.exception.InvalidBarcodeException;
import com.firatdemir.model.Product;
import com.firatdemir.service.ProductService;
import com.firatdemir.starter.FiyatRadarApplication;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = FiyatRadarApplication.class)
@Transactional
public class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Test
	void contextLoads() {
		// Spring konteynerinin düzgün bir şekilde başlatıldığını test eder
	}

	@Test
	void testProductServiceNotNull() {
		assertNotNull(productService, "ProductService null olmamalıdır");
	}

	@Test
	void testSaveProduct() {
		String uniqueBarcode = "1234567842" + System.currentTimeMillis(); // Benzersiz barkod
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductName("Test Ürünü");
		productDTO.setBarcode(uniqueBarcode); // Benzersiz barkod
		productDTO.setDescription("Örnek açıklama");
		productDTO.setPrice(19.99);
		productDTO.setStoreName("Örnek Mağaza");

		Product savedProduct = productService.saveProduct(productDTO);

		assertNotNull(savedProduct, "Kaydedilen ürün null olmamalıdır");
		assertNotNull(savedProduct.getId(), "Kaydedilen ürünün ID'si olmalıdır");
	}

	@Test
	void testSaveProductWithInvalidBarcode() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductName("Test Ürünü");
		productDTO.setBarcode("12345678901"); // Geçersiz barkod (11 haneli)
		productDTO.setDescription("Örnek açıklama");
		productDTO.setPrice(19.99);
		productDTO.setStoreName("Örnek Mağaza");

		// Geçersiz barkod ile ürün kaydetmeye çalıştığımızda hata fırlatılmalı
		assertThrows(InvalidBarcodeException.class, () -> productService.saveProduct(productDTO));
	}

	@Test
	void testSaveProductWithCorrectPrice() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductName("Test Ürünü");
		productDTO.setBarcode("123456789012");
		productDTO.setDescription("Örnek açıklama");
		productDTO.setPrice(19.99); // Fiyat doğru
		productDTO.setStoreName("Örnek Mağaza");

		Product savedProduct = productService.saveProduct(productDTO);

		assertNotNull(savedProduct);
		assertEquals(19.99, savedProduct.getPrice(), 0.01, "Ürün fiyatı 19.99 olmalıdır");
		
	}
}
