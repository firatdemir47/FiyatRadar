package com.firatdemir.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	 @Column(unique = true)
	private String barcode;  // Ürünün barkodu
	
	 @Column(name = "category")
	    private String category;
	 
	private String name;  // Ürünün adı
	
	private String description; // Ürünün açıklaması
	
	private double price;  // Ürünün fiyatı	
	
	private String storeName; // Ürünün satıldığı marketin adı

}
