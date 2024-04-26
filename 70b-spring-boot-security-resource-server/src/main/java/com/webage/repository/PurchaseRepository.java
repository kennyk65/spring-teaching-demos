package com.webage.repository;

import org.springframework.data.repository.CrudRepository;

import com.webage.domain.Purchase;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {

}
