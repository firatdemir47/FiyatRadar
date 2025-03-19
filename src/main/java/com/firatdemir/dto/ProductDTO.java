package com.firatdemir.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

	@NotNull(message = "Ürün adı boş olamaz.")
	private String productName;

	@NotNull(message = "Barkod boş olamaz.")
	@Size(min = 12, max = 12, message = "Barkod 12 haneli olmalıdır.")
	private String barcode;

	private String description;

	@Positive(message = "Fiyat pozitif olmalıdır.")
	private double price;

	private String storeName;

}
