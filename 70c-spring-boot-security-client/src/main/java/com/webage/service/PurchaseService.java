package com.webage.service;

import java.util.Optional;

import com.webage.domain.Purchase;

public interface PurchaseService {
	public Iterable<Purchase> findAllPurchases();
	public Optional<Purchase> findPurchaseById(long id);
}
