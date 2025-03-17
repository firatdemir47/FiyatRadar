package com.firatdemir.FiyatRadar;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.firatdemir.model.Product;
import com.firatdemir.service.ProductService;
import com.firatdemir.starter.FiyatRadarApplication;

/*@SpringBootTest(classes = FiyatRadarApplication.class)
class FiyatRadarApplicationTests {
      
	   @Autowired
	    private ProductService productService;
	   
	@Test
	void contextLoads() {
		// Bu metod, Spring konteynerinin başarılı bir şekilde başlatıldığını kontrol eder
	}
	
	   @Test
	    void testProductServiceNotNull() {
	        // ProductService sınıfının doğru şekilde enjekte edildiğini kontrol ederiz
	        assertNotNull(productService, "ProductService should not be null");
	    }

		   @Test
		    void testSaveProduct() {
		        // Yeni bir ürün oluşturup, kaydediyoruz
				Product product = new Product();
		        product.setName("Test Product");
		        product.setPrice(19.99);
		        product.setBarcode("1234567890");
	
		        // Ürünü kaydediyoruz
		        Product savedProduct = productService.saveProduct(product);
	
		        // Ürünün null olmaması gerektiğini kontrol ediyoruz
		        assertNotNull(savedProduct, "Saved product should not be null");
	
		        // Ürünün ID'sinin null olmaması gerektiğini kontrol ediyoruz
		        assertNotNull(savedProduct.getId(), "Saved product should have an ID");
		    }
}
*/