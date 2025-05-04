package com.firatdemir.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String barcode; // Ürünün barkodu

	@Column(name = "category")
	private String category;

	private String brand;

	private String name; // Ürünün adı

	private Double unitPrice;

	private String description; // Ürünün açıklaması

	private String quantity;
	
	private String unit;

	private Integer merchantId;

	private double price; // Ürünün fiyatı

	private String storeName; // Ürünün satıldığı marketin adı

	private String merchantLogo;

	private String image;

	private String url;
	/*
	 * @PositiveOrZero(message = "Stok miktarı negatif olamaz.") private int stock;
	 * // Ürünün stok miktarı
	 */
}
