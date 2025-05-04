package com.firatdemir.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.firatdemir.dto.ProductDTO;
import com.firatdemir.model.Product;
import com.firatdemir.service.CimriClientService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/product")
public class ProductSearchController {

	private final CimriClientService cimriClientService;

	public ProductSearchController(CimriClientService cimriClientService) {
		this.cimriClientService = cimriClientService;
	}


	// Barkod ile ürün arama
    @Operation(summary = "Barkod ile ürün arama", description = "Barkod bilgisi ile bir ürünü arar.")
    @GetMapping("/searchByBarcode")
    public ResponseEntity<ProductDTO> searchByBarcode(@RequestParam String barcode) {
        // Barkod bilgisi ile ürün arama işlemi yapılır
        Product product = cimriClientService.searchProductByBarcode(barcode);
        
        if (product != null) {
            // Product'tan ProductDTO'ya dönüşüm yapılır
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductName(product.getName());
            productDTO.setBarcode(product.getBarcode());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStoreName(product.getStoreName());

            return ResponseEntity.ok(productDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/fetchAll")
    public ResponseEntity<List<Product>> fetchAllProducts() {
        List<Product> products = cimriClientService.fetchAllProducts();
        return ResponseEntity.ok(products);
    }
    
	//istek attığımızda tüm sayfayı veri tabanına ekler 
	@GetMapping("/search")
	public ResponseEntity<List<Product>> search(@RequestParam String q) {
		List<Product> products = cimriClientService.searchProduct(q);
		return ResponseEntity.ok(products);
	}
}
