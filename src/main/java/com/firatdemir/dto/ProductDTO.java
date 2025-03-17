package com.firatdemir.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
		
	private String productName;
	
	private String barcode;

	 private String description;
	   
	 private double price;
	    
	 private String storeName;
	
}
