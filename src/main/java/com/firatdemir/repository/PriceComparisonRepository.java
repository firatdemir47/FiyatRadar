package com.firatdemir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firatdemir.model.PriceComparasion;
import com.firatdemir.model.Product;

@Repository
public interface PriceComparisonRepository extends JpaRepository<PriceComparasion, Long> {
	
}
