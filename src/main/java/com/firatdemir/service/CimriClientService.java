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
		List<Product> allProducts = new ArrayList<>();

		int currentPage = 1;
		int totalPages = 1;

		do {
			String url = "http://127.0.0.1:8000/api.php?q=" + UriUtils.encode(query, StandardCharsets.UTF_8) + "&page="
					+ currentPage;

			ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
			JsonNode root = response.getBody();

			JsonNode pagination = root.path("pagination");
			currentPage = pagination.path("current_page").asInt();
			totalPages = pagination.path("total_pages").asInt();

			JsonNode productsNode = root.path("products");

			for (JsonNode productNode : productsNode) {
				String barcode = productNode.path("id").asText();

				try {
					// Barkod zaten varsa bu ürünü atla
					if (productRepository.existsByBarcode(barcode)) {
						continue;
					}

					Product product = new Product();

					product.setBarcode(barcode);
					product.setCategory(productNode.path("category").asText(""));
					product.setBrand(productNode.path("brand").asText(""));
					product.setName(productNode.path("name").asText(""));
					product.setUnitPrice(productNode.path("unit_price").asDouble());
					product.setDescription(productNode.path("description").asText(""));
					product.setQuantity(productNode.path("quantity").asText(""));
					product.setUnit(productNode.path("unit").asText(""));
					product.setMerchantId(productNode.path("merchant_id").asInt(0));
					product.setPrice(productNode.path("price").asDouble());
					product.setStoreName(productNode.path("merchant_logo").asText(""));
					product.setMerchantLogo(productNode.path("merchant_logo").asText(""));
					product.setImage(productNode.path("image").asText(""));
					product.setUrl(productNode.path("url").asText(""));

					productRepository.save(product);
					allProducts.add(product);

				} catch (Exception e) {
					// Hata varsa logla, işlemi kesmeden devam et
					System.err.println("Ürün eklenemedi (barkod: " + barcode + "): " + e.getMessage());
				}
			}

			currentPage++;

		} while (currentPage <= totalPages);

		return allProducts;
	}

	

	public List<Product> fetchAllProducts() {
		List<Product> allProducts = new ArrayList<>();
		String url = "http://127.0.0.1:8000/api.php?q=" + UriUtils.encode("", StandardCharsets.UTF_8);
		ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

		JsonNode productsNode = response.getBody().path("products");

		for (JsonNode productNode : productsNode) {
			Product product = new Product();

			// API'den gelen her bir ürünü işleyerek Product objesine dönüştür
			product.setBarcode(productNode.path("id").asText()); // Barkod
			product.setCategory(productNode.path("category").asText()); // Kategori
			product.setBrand(productNode.path("brand").asText()); // Marka
			product.setName(productNode.path("name").asText()); // Ürün adı
			product.setUnitPrice(productNode.path("unitPrice").asDouble()); // Birim fiyat
			product.setDescription(productNode.path("description").asText()); // Açıklama
			product.setQuantity(productNode.path("quantity").asText()); // Miktar
			product.setUnit(productNode.path("unit").asText()); // Birim
			product.setMerchantId(productNode.path("merchantId").asInt()); // Satıcı ID
			product.setPrice(productNode.path("price").asDouble()); // Fiyat
			product.setStoreName(productNode.path("storeName").asText()); // Mağaza adı
			product.setMerchantLogo(productNode.path("merchantLogo").asText()); // Mağaza logosu
			product.setImage(productNode.path("image").asText()); // Resim
			product.setUrl(productNode.path("url").asText()); // URL

			// Ürünü veritabanına kaydet
			productRepository.save(product);
			allProducts.add(product);
		}

		return allProducts;
	}

	// Barkod ile ürün arama servisi
	public Product searchProductByBarcode(String barcode) {
		String apiUrl = "http://cimri.com/api/product/searchByBarcode?barcode=" + barcode;

		// API'den veri çekiyoruz
		ResponseEntity<Product> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Product.class);

		return response.getBody();
	}
}
