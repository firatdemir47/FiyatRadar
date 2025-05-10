package com.firatdemir.service;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

	/*
	 * //eğer o ürün o gün güncellendi ise o ürünü atlar burası daha sonra aktif
	 * edilecek
	 * 
	 * @Scheduled(cron = "0 0 2 * * *") // Her gün sabah 02:00'de çalışır public
	 * void updateAllPriceComparisons() { List<Product> allProducts =
	 * productRepository.findAll();
	 * 
	 * // URL'leri tekilleştiriyoruz List<String> distinctUrls =
	 * allProducts.stream() .map(Product::getUrl) .distinct() .toList();
	 * 
	 * for (String encodedUrl : distinctUrls) { // Bu URL'ye sahip olan ürünü al
	 * Product relatedProduct = productRepository.findByUrl(encodedUrl);
	 * 
	 * // Ürün null ise geç if (relatedProduct == null) {
	 * System.out.println("Ürün bulunamadı: " + encodedUrl); continue; }
	 * 
	 * // Eğer ürün zaten bugün güncellenmişse, istek atma if
	 * (LocalDate.now().equals(relatedProduct.getLastUpdated())) {
	 * System.out.println("Bugün zaten güncellenmiş: " + relatedProduct.getId());
	 * continue; }
	 * 
	 * String productPath = encodedUrl.replace("/product.php?path=", "")
	 * .replace("%2F", "/") .replace("%2C", ",");
	 * 
	 * URI uri =
	 * UriComponentsBuilder.fromHttpUrl("http://localhost:8000/product.php")
	 * .queryParam("path", productPath) .build().encode().toUri();
	 * 
	 * try { System.out.println("İstek atılıyor: " + uri); String response =
	 * restTemplate.getForObject(uri, String.class);
	 * 
	 * JsonNode root = objectMapper.readTree(response); JsonNode offers =
	 * root.path("product").path("offers");
	 * 
	 * if (offers.isArray()) { for (JsonNode offer : offers) { String storeName =
	 * offer.path("merchant_name").asText(); double price =
	 * offer.path("price").asDouble();
	 * 
	 * Optional<PriceComparasion> existing = priceComparisonRepository
	 * .findByStoreNameAndProduct(storeName, relatedProduct);
	 * 
	 * if (existing.isPresent()) { // Mevcut kaydın fiyatını güncelle
	 * PriceComparasion pc = existing.get(); pc.setPrice(price);
	 * priceComparisonRepository.save(pc); System.out.println("Fiyat güncellendi: "
	 * + storeName + " - " + price + " -> product: " + relatedProduct.getId());
	 * continue; }
	 * 
	 * // Yeni kayıt oluştur PriceComparasion pc = new PriceComparasion();
	 * pc.setProduct(relatedProduct); pc.setStoreName(storeName);
	 * pc.setPrice(price); pc.setUrl(encodedUrl);
	 * 
	 * priceComparisonRepository.save(pc); System.out.println("Eklendi: " +
	 * storeName + " - " + price + " -> product: " + relatedProduct.getId()); }
	 * 
	 * // Ürünün güncelleme tarihini bugünün tarihi yap
	 * relatedProduct.setLastUpdated(LocalDate.now());
	 * productRepository.save(relatedProduct); } else {
	 * System.out.println("Fiyat bilgisi yok: " + uri); }
	 * 
	 * } catch (HttpClientErrorException e) { System.out.println("HTTP Hatası: " +
	 * e.getStatusCode() + " - " + e.getResponseBodyAsString()); } catch (Exception
	 * e) { System.out.println("Hata: " + e.getMessage()); } } }
	 */
	
	// her gün 2 de istek atacak şekilde zamanlama kuruldu
	@Scheduled(cron = "0 0 2 * * *") // Her gün sabah 02:00'de çalışır
	public void updateAllPriceComparisons() {
		List<Product> allProducts = productRepository.findAll();

		// URL'leri tekilleştiriyoruz
		List<String> distinctUrls = allProducts.stream().map(Product::getUrl).distinct().toList();

		for (String encodedUrl : distinctUrls) {
			String productPath = encodedUrl.replace("/product.php?path=", "").replace("%2F", "/").replace("%2C", ",");

			URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8000/product.php")
					.queryParam("path", productPath).build().encode().toUri();

			try {
				System.out.println("İstek atılıyor: " + uri);
				String response = restTemplate.getForObject(uri, String.class);

				JsonNode root = objectMapper.readTree(response);
				JsonNode offers = root.path("product").path("offers");

				if (offers.isArray()) {
					// Bu URL'ye sahip olan ürünü al
					Product relatedProduct = productRepository.findByUrl(encodedUrl);

					// Eğer ürün bulunduysa
					if (relatedProduct != null) {
						for (JsonNode offer : offers) {
							String storeName = offer.path("merchant_name").asText();
							double price = offer.path("price").asDouble();

							Optional<PriceComparasion> existing = priceComparisonRepository
									.findByStoreNameAndProduct(storeName, relatedProduct);

							if (existing.isPresent()) {
								// Mevcut kaydın fiyatını güncelle
								PriceComparasion pc = existing.get();
								pc.setPrice(price); // Fiyatı güncelle
								priceComparisonRepository.save(pc); // Güncellenmiş veriyi kaydet
								System.out.println("Fiyat güncellendi: " + storeName + " - " + price + " -> product: "
										+ relatedProduct.getId());
								continue;
							}

							PriceComparasion pc = new PriceComparasion();
							pc.setProduct(relatedProduct);
							pc.setStoreName(storeName);
							pc.setPrice(price);
							pc.setUrl(encodedUrl);

							priceComparisonRepository.save(pc);
							System.out.println(
									"Eklendi: " + storeName + " - " + price + " -> product: " + relatedProduct.getId());
						}
					} else {
						System.out.println("Ürün bulunamadı: " + encodedUrl);
					}
				} else {
					System.out.println("Fiyat bilgisi yok: " + uri);
				}

			} catch (HttpClientErrorException e) {
				System.out.println("HTTP Hatası: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
			} catch (Exception e) {
				System.out.println("Hata: " + e.getMessage());
			}
		}
	}

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
