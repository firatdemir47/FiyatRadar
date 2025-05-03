package com.firatdemir.mapper;

import org.springframework.stereotype.Component;

import com.firatdemir.dto.ProductDTO;
import com.firatdemir.model.Product;

@Component
public class ProductMapperImpl implements ProductMapper {

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
        product.setCategory(productDTO.getCategory());
        product.setBrand(productDTO.getBrand());
        product.setUnitPrice(productDTO.getUnitPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setUnit(productDTO.getUnit());
        product.setMerchantId(productDTO.getMerchantId());
        product.setMerchantLogo(productDTO.getMerchantLogo());
        product.setImage(productDTO.getImage());
        product.setUrl(productDTO.getUrl());
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
        productDTO.setCategory(product.getCategory());
        productDTO.setBrand(product.getBrand());
        productDTO.setUnitPrice(product.getUnitPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setUnit(product.getUnit());
        productDTO.setMerchantId(product.getMerchantId());
        productDTO.setMerchantLogo(product.getMerchantLogo());
        productDTO.setImage(product.getImage());
        productDTO.setUrl(product.getUrl());
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
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setBrand(productDTO.getBrand());
        existingProduct.setUnitPrice(productDTO.getUnitPrice());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setUnit(productDTO.getUnit());
        existingProduct.setMerchantId(productDTO.getMerchantId());
        existingProduct.setMerchantLogo(productDTO.getMerchantLogo());
        existingProduct.setImage(productDTO.getImage());
        existingProduct.setUrl(productDTO.getUrl());
    }
}

/*
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
        // Eğer category bilgisi gerekiyorsa, buraya ekleyebiliriz
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
}*/
