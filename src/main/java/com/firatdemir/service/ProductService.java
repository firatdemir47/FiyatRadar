package com.firatdemir.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firatdemir.exception.ResourceNotFoundException;
import com.firatdemir.model.Product;
import com.firatdemir.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Ürün ekleme
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Tüm ürünleri listeleme
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ID'ye göre ürün bulma
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    // Ürün güncelleme
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setBarcode(product.getBarcode());
        existingProduct.setPrice(product.getPrice());
        return productRepository.save(existingProduct);
    }

   
    
    public void deleteProduct(Long id) {
        Product existingProduct = getProductById(id);
        productRepository.delete(existingProduct);
    }
}
