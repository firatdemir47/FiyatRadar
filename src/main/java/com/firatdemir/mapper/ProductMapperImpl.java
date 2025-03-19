package com.firatdemir.mapper;

import org.springframework.stereotype.Component;

import com.firatdemir.dto.ProductDTO;
import com.firatdemir.model.Product;

@Component
public class ProductMapperImpl  implements ProductMapper{
	@Override
    public Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        Product product = new Product();
        product.setName(productDTO.getProductName());
        product.setStoreName(productDTO.getStoreName());
        product.setBarcode(productDTO.getBarcode());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        // Eğer category bilgisi gerekiyorsa, buraya ekleyebilirsiniz.
        return product;
    }

    @Override
    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName(product.getName());
        productDTO.setStoreName(product.getStoreName());
        productDTO.setBarcode(product.getBarcode());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        return productDTO;
    }

    @Override
    public void updateEntityFromDTO(ProductDTO productDTO, Product existingProduct) {
        if (productDTO == null || existingProduct == null) {
            return;
        }
        existingProduct.setName(productDTO.getProductName());
        existingProduct.setStoreName(productDTO.getStoreName());
        existingProduct.setBarcode(productDTO.getBarcode());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
    }
}
