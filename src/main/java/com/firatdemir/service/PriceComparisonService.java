package com.firatdemir.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firatdemir.model.PriceComparasion;
import com.firatdemir.model.Product;
import com.firatdemir.repository.PriceComparisonRepository;
import com.firatdemir.repository.ProductRepository;

@Service
public class PriceComparisonService {

	private final PriceComparisonRepository priceComparisonRepository;
	private final ProductRepository productRepository;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Autowired
	public PriceComparisonService(ProductRepository productRepository,
			PriceComparisonRepository priceComparisonRepository) {
		this.priceComparisonRepository = priceComparisonRepository;
		this.productRepository = productRepository;
		this.restTemplate = new RestTemplate();
		this.objectMapper = new ObjectMapper();
	}
	
	public void updateAllPriceComparisons() {
        // Tüm ürünleri veri tabanından çek
        List<Product> allProducts = productRepository.findAll();
        
        for (Product product : allProducts) {
            String encodedProductUrl = product.getUrl(); // Ör: /product.php?path=bal-ve-recel%2Fen-ucuz-...
            // productPath'i oluştur: Ör: bal-ve-recel/en-ucuz-...
            String productPath = encodedProductUrl.replace("/product.php?path=", "")
                                                 .replace("%2F", "/")
                                                 .replace("%2C", ",");

            URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8000/product.php")
                    .queryParam("path", productPath)
                    .build().encode().toUri();

            try {
                System.out.println("İstek atılıyor: " + uri);
                String response = restTemplate.getForObject(uri, String.class);
                System.out.println("Yanıt alındı for product " + product.getId() + ": " + response);

                JsonNode root = objectMapper.readTree(response);
                JsonNode offers = root.path("product").path("offers");

                if (offers.isArray()) {
                    // Ürün URL'si ile ürünü bul
                    Product existingProduct = productRepository.findByUrl(encodedProductUrl);
                    if (existingProduct == null) {
                        System.out.println("Ürün bulunamadı: " + encodedProductUrl);
                        continue;
                    }

                    for (JsonNode offer : offers) {
                        String storeName = offer.path("merchant_name").asText();
                        double price = offer.path("price").asDouble();

                        // Aynı mağaza ve ürün kombinasyonunu kontrol et
                        Optional<PriceComparasion> existingComparison = priceComparisonRepository
                                .findByStoreNameAndProduct(storeName, existingProduct);

                        if (existingComparison.isPresent()) {
                            System.out.println("Bu mağaza ve ürün kombinasyonu zaten mevcut: " + storeName + 
                                              " for product: " + existingProduct.getId());
                            continue;
                        }

                        // Yeni fiyat karşılaştırması oluştur ve kaydet
                        PriceComparasion comparison = new PriceComparasion();
                        comparison.setProduct(existingProduct);
                        comparison.setStoreName(storeName);
                        comparison.setPrice(price);
                        comparison.setUrl(encodedProductUrl);

                        priceComparisonRepository.save(comparison);
                        System.out.println("Fiyat eklendi: " + storeName + " - " + price + 
                                          " for product: " + existingProduct.getId());
                    }
                } else {
                    System.out.println("Fiyat bilgisi bulunamadı: " + uri);
                }
            } catch (HttpClientErrorException e) {
                System.out.println("HTTP Hatası for product " + product.getId() + ": " + 
                                  e.getStatusCode() + " - " + e.getResponseBodyAsString());
            } catch (Exception e) {
                System.out.println("Genel Hata for product " + product.getId() + ": " + e.getMessage());
            }
        }
    }



	/*public void updateSinglePriceComparison() {
		String productPath = "sebze/en-ucuz-domates-kg-fiyatlari,1173365";
		String productUrl = "/product.php";
		String encodedProductUrl = "/product.php?path=sebze%2Fen-ucuz-domates-kg-fiyatlari%2C1173365";
		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8000" + productUrl).queryParam("path", productPath)
				.build().encode().toUri();

		try {
			System.out.println("İstek atılıyor: " + uri);
			String response = restTemplate.getForObject(uri, String.class);
			System.out.println("Yanıt alındı: " + response);

			JsonNode root = objectMapper.readTree(response);
			JsonNode offers = root.path("product").path("offers");

			if (offers.isArray()) {
				// Ürün URL'si ile ürünü buluyoruz
				Product product = productRepository.findByUrl(encodedProductUrl);
				if (product == null) {
					System.out.println("Ürün bulunamadı: " + encodedProductUrl);
					return;
				}

				for (JsonNode offer : offers) {
					String storeName = offer.path("merchant_name").asText();
					double price = offer.path("price").asDouble();

					// Aynı mağaza ve ürün kombinasyonunun daha önce kaydedilip kaydedilmediğini
					// kontrol et
					Optional<PriceComparasion> existingComparison = priceComparisonRepository
							.findByStoreNameAndProduct(storeName, product);

					if (existingComparison.isPresent()) {
						System.out.println("Bu mağaza ve ürün kombinasyonu zaten mevcut: " + storeName);
						continue; // Eğer varsa, bu mağaza için yeni fiyat eklemiyoruz
					}

					// Yeni fiyat karşılaştırmasını oluştur ve kaydet
					PriceComparasion comparison = new PriceComparasion();
					comparison.setProduct(product);
					comparison.setStoreName(storeName);
					comparison.setPrice(price);
					comparison.setUrl(encodedProductUrl);

					priceComparisonRepository.save(comparison);
					System.out.println("Fiyat eklendi: " + storeName + " - " + price);
				}
			} else {
				System.out.println("Fiyat bilgisi bulunamadı: " + uri);
			}
		} catch (HttpClientErrorException e) {
			System.out.println("HTTP Hatası: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
		} catch (Exception e) {
			System.out.println("Genel Hata: " + e.getMessage());
		}
	}*/

	public List<PriceComparasion> getByProductId(Long productId) {
		return priceComparisonRepository.findByProductId(productId);
	}

	// Fiyat karşılaştırma ekleme
	public PriceComparasion savePriceComparasion(PriceComparasion priceComparasion) {
		return priceComparisonRepository.save(priceComparasion);
	}

	// Fiyat Karşılaştırmalarını listeleme
	public List<PriceComparasion> getAllPriceComparasions() {
		return priceComparisonRepository.findAll();
	}
}
