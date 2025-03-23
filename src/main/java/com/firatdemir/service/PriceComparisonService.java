package com.firatdemir.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firatdemir.model.PriceComparasion;
import com.firatdemir.repository.PriceComparisonRepository;

@Service
public class PriceComparisonService {

	private final PriceComparisonRepository priceComparisonRepository;

	@Autowired
	public PriceComparisonService(PriceComparisonRepository priceComparisonRepository) {
		this.priceComparisonRepository = priceComparisonRepository;
	}

	// Fiyat karşılaştırma ekleme
	public PriceComparasion savePriceComparasion(PriceComparasion priceComparasion) {
		return priceComparisonRepository.save(priceComparasion);
	}

	// Fiyat Karşılaştırmalarını listeleme
	public List<PriceComparasion> getAllPriceComparasions() {
		return priceComparisonRepository.findAll();
	}
}
