package com.firatdemir.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriceComparisonDTO {
			
	private String productBarcode;
	
	private double price ;
	
	private String storeName;
	
}
