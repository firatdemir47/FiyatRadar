package com.firatdemir.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	 @Column(unique = true)
	private String barcode;  // Ürünün barkodu
	
	private String name;  // Ürünün adı
	
	private String description; // Ürünün açıklaması
	
	private double price;  // Ürünün fiyatı	
	
	private String storeName; // Ürünün satıldığı marketin adı

}
