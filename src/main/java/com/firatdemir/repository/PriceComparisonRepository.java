package com.firatdemir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firatdemir.model.PriceComparasion;

@Repository
public interface PriceComparisonRepository extends JpaRepository<PriceComparasion, Long> {

}
