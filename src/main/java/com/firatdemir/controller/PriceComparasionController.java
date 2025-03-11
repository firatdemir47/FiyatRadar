package com.firatdemir.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firatdemir.model.PriceComparasion;
import com.firatdemir.service.PriceComparisonService;

@RestController
@RequestMapping("/api/price-comparisons")
public class PriceComparasionController {

	private final PriceComparisonService priceComparisonService;

	@Autowired
	public PriceComparasionController(PriceComparisonService priceComparisonService) {
		this.priceComparisonService = priceComparisonService;
	}

	// fiyat karşılaştırma ekleme
	@PostMapping
	public ResponseEntity<PriceComparasion> createPriceComparison(@RequestBody PriceComparasion priceComparasion) {
		PriceComparasion createdPriceComparasion = priceComparisonService.savePriceComparasion(priceComparasion);
		return new ResponseEntity<>(createdPriceComparasion, HttpStatus.CREATED);
	}
	
	//tüm fiyat karşılaştırmaları ekleme 
	@GetMapping
	public List<PriceComparasion> getAllPriceComparisons(){
		return priceComparisonService.getAllPriceComparasions();
	}
}
