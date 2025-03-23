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
	// test sınıfı sonradan güncellenecek
	@Autowired
	private ProductService productService;

	@Test
	void contextLoads() {
		// Spring konteynerinin düzgün bir şekilde başlatıldığını test eder
	}

	@Test
	void testProductServiceNotNull() {
		// ProductService'in doğru şekilde enjekte edilip edilmediğini test eder
		assertNotNull(productService, "ProductService null olmamalıdır");
	}

	@Test
	void testSaveProduct() {
		// Benzersiz bir barkod oluşturuyoruz
		String uniqueBarcode = "1234567842" + System.currentTimeMillis(); // Benzersiz barkod

		// ProductDTO oluşturuyoruz
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductName("Test Ürünü");
		productDTO.setBarcode(uniqueBarcode); // Benzersiz barkod
		productDTO.setDescription("Örnek açıklama");
		productDTO.setPrice(19.99);
		productDTO.setStoreName("Örnek Mağaza");

		// Ürünü kaydediyoruz
		Product savedProduct = productService.saveProduct(productDTO);

		// Ürünün null olmaması gerektiğini kontrol ediyoruz
		assertNotNull(savedProduct, "Kaydedilen ürün null olmamalıdır");

		// Ürünün ID'sinin null olmaması gerektiğini kontrol ediyoruz
		assertNotNull(savedProduct.getId(), "Kaydedilen ürünün ID'si olmalıdır");
	}

	@Test
	void testSaveProductWithInvalidBarcode() {
		// Geçersiz barkod: 12 haneli olması gereken barkod 11 haneli
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductName("Test Ürünü");
		productDTO.setBarcode("12345678901"); // Geçersiz barkod (11 haneli)
		productDTO.setDescription("Örnek açıklama");
		productDTO.setPrice(19.99);
		productDTO.setStoreName("Örnek Mağaza");

		// Hata bekliyoruz çünkü barkod geçersiz
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

		// Fiyatın doğru kaydedildiğini kontrol ediyoruz
		assertNotNull(savedProduct);
		// assertEquals ile fiyatı kontrol ederken delta ekliyoruz
		assertEquals(19.99, savedProduct.getPrice(), 0.01, "Ürün fiyatı 19.99 olmalıdır");
	}

	// assertEquals metodunu manuel yazıyoruz
	private void assertEquals(double expected, double actual, double delta, String message) {
		if (Math.abs(expected - actual) > delta) {
			throw new AssertionError(message + " Beklenen: " + expected + " ancak gelen: " + actual);
		}
	}

	// assertThrows metodunu manuel yazıyoruz
	private void assertThrows(Class<? extends Throwable> expectedException, Runnable executable) {
		try {
			executable.run(); // Test edilen kodu çalıştır
			throw new AssertionError(
					"Beklenen istisna: " + expectedException.getName() + " ancak hiçbir şey fırlatılmadı.");
		} catch (Throwable actualException) {
			if (!expectedException.isInstance(actualException)) {
				throw new AssertionError("Beklenen istisna: " + expectedException.getName() + " ancak gelen: "
						+ actualException.getClass().getName());
			}
		}
	}
}