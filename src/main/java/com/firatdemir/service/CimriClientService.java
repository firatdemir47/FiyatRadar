package com.firatdemir.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.firatdemir.model.Product;
import com.firatdemir.repository.ProductRepository;

@Service
public class CimriClientService {

	private final RestTemplate restTemplate;
	private final ProductRepository productRepository;

	public CimriClientService(RestTemplate restTemplate, ProductRepository productRepository) {
		this.restTemplate = restTemplate;
		this.productRepository = productRepository;
	}

	public List<Product> searchProduct(String query) {
		String url = "http://127.0.0.1:8000/api.php?q=" + UriUtils.encode(query, StandardCharsets.UTF_8);
		ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

		JsonNode productsNode = response.getBody().path("products");
		List<Product> products = new ArrayList<>();

		// API'den gelen her bir ürünü işleyerek Product objesine dönüştür
		for (JsonNode productNode : productsNode) {
			Product product = new Product();
			product.setBarcode(productNode.path("id").asText()); // Eğer API'deki id barkod ise
			product.setCategory(productNode.path("category").asText());
			product.setName(productNode.path("name").asText());
			product.setDescription(productNode.path("description").asText());
			product.setPrice(productNode.path("price").asDouble());
			
			product.setStoreName(productNode.path("merchant_logo").asText()); // Ya da mağaza adı

			// Veritabanına kaydedin
			productRepository.save(product);
			products.add(product);
		}

		return products;
	}
	// Barkod ile ürün arama servisi
    public Product searchProductByBarcode(String barcode) {
        String apiUrl = "http://cimri.com/api/product/searchByBarcode?barcode=" + barcode;
        
        // API'den veri çekiyoruz
        ResponseEntity<Product> response = restTemplate.exchange(
                apiUrl, HttpMethod.GET, null, Product.class);

        return response.getBody();
    }
}
