package com.firatdemir.FiyatRadar;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
@Transactional // Test sonrası yapılan değişiklikler geri alınır
public class ProductServiceTest {
	//test sınıfı sonradan güncellenecek 
	@Autowired
	private ProductService productService;

	@Test
	void contextLoads() {
		// Spring konteynerinin düzgün bir şekilde başladığını test eder
	}

	@Test
	void testProductServiceNotNull() {
		// ProductService'in doğru şekilde enjekte edilip edilmediğini test eder
		assertNotNull(productService, "ProductService should not be null");
	}

	@Test
	void testSaveProduct() {
		// Benzersiz bir barkod oluşturuyoruz
	

		String uniqueBarcode = "1234567842" + System.currentTimeMillis(); // Benzersiz barkod

		// ProductDTO oluşturuyoruz
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductName("Test Product");
		productDTO.setBarcode(uniqueBarcode); // Benzersiz barkod
		productDTO.setDescription("Sample description");
		productDTO.setPrice(19.99);
		productDTO.setStoreName("Sample Store");

		// Ürünü kaydediyoruz
		Product savedProduct = productService.saveProduct(productDTO);

		// Ürünün null olmaması gerektiğini kontrol ediyoruz
		assertNotNull(savedProduct, "Saved product should not be null");

		// Ürünün ID'sinin null olmaması gerektiğini kontrol ediyoruz
		assertNotNull(savedProduct.getId(), "Saved product should have an ID");
	}

	@Test
	void testSaveProductWithInvalidBarcode() {
		// Geçersiz barkod: 12 haneli olması gereken barkod 11 haneli
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductName("Test Product");
		productDTO.setBarcode("12345678901"); // Geçersiz barkod (11 haneli)
		productDTO.setDescription("Sample description");
		productDTO.setPrice(19.99);
		productDTO.setStoreName("Sample Store");

		// Hata bekliyoruz çünkü barkod geçersiz
		assertThrows(InvalidBarcodeException.class, () -> productService.saveProduct(productDTO));
	}

	@Test
	void testSaveProductWithCorrectPrice() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductName("Test Product");
		productDTO.setBarcode("123456789012");
		productDTO.setDescription("Sample description");
		productDTO.setPrice(19.99); // Fiyat doğru
		productDTO.setStoreName("Sample Store");

		Product savedProduct = productService.saveProduct(productDTO);

		// Fiyatın doğru kaydedildiğini kontrol ediyoruz
		assertNotNull(savedProduct);
		// assertEquals ile fiyatı kontrol ederken delta ekliyoruz
		assertEquals(19.99, savedProduct.getPrice(), 0.01, "The product price should be 19.99");
	}

	// assertEquals metodunu manuel yazıyoruz
	private void assertEquals(double expected, double actual, double delta, String message) {
		if (Math.abs(expected - actual) > delta) {
			throw new AssertionError(message + " Expected: " + expected + " but got: " + actual);
		}
	}

	// assertThrows metodunu manuel yazıyoruz
	private void assertThrows(Class<? extends Throwable> expectedException, Runnable executable) {
		try {
			executable.run(); // Test edilen kodu çalıştır
			throw new AssertionError("Expected exception: " + expectedException.getName() + " but nothing was thrown.");
		} catch (Throwable actualException) {
			if (!expectedException.isInstance(actualException)) {
				throw new AssertionError("Expected exception: " + expectedException.getName() + " but got: "
						+ actualException.getClass().getName());
			}
		}
	}
}
