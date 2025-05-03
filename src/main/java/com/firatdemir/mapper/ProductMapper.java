package com.firatdemir.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.firatdemir.dto.ProductDTO;
import com.firatdemir.model.Product;
	
@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productName", target = "name")
    @Mapping(source = "barcode", target = "barcode")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "storeName", target = "storeName")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unit", target = "unit")
    @Mapping(source = "merchantId", target = "merchantId")
    @Mapping(source = "merchantLogo", target = "merchantLogo")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "url", target = "url")
    Product toEntity(ProductDTO productDTO);

    @Mapping(source = "name", target = "productName")
    @Mapping(source = "barcode", target = "barcode")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "storeName", target = "storeName")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unit", target = "unit")
    @Mapping(source = "merchantId", target = "merchantId")
    @Mapping(source = "merchantLogo", target = "merchantLogo")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "url", target = "url")
    ProductDTO toDTO(Product product);

    @Mapping(source = "productName", target = "name")
    @Mapping(source = "barcode", target = "barcode")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "storeName", target = "storeName")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unit", target = "unit")
    @Mapping(source = "merchantId", target = "merchantId")
    @Mapping(source = "merchantLogo", target = "merchantLogo")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "url", target = "url")
    void updateEntityFromDTO(ProductDTO productDTO, @MappingTarget Product existingProduct);
}
	

/*@Mapper(componentModel = "spring")
	public interface ProductMapper {
		
	    @Mapping(source = "productName", target = "name")
	    @Mapping(source = "barcode", target = "barcode")
	    @Mapping(source = "description", target = "description")
	    @Mapping(source = "price", target = "price")
	    @Mapping(source = "storeName", target = "storeName")
	    Product toEntity(ProductDTO productDTO);

	    @Mapping(source = "name", target = "productName")
	    @Mapping(source = "barcode", target = "barcode")
	    @Mapping(source = "description", target = "description")
	    @Mapping(source = "price", target = "price")
	    @Mapping(source = "storeName", target = "storeName")
	    ProductDTO toDTO(Product product);

	    @Mapping(source = "productName", target = "name")
	    @Mapping(source = "barcode", target = "barcode")
	    @Mapping(source = "description", target = "description")
	    @Mapping(source = "price", target = "price")
	    @Mapping(source = "storeName", target = "storeName")
	    void updateEntityFromDTO(ProductDTO productDTO, @MappingTarget Product existingProduct);
	}*/
