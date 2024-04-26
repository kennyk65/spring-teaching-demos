package com.webage.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webage.domain.Purchase;
import com.webage.repository.PurchaseRepository;

@Service
public class PurchaseServiceImpl implements PurchaseService {
	@Autowired
	private PurchaseRepository repo;


	public void savePurchase(Purchase purchase) {
		repo.save(purchase);
	}


	public Iterable<Purchase> findAllPurchases() {
		return repo.findAll();
	}


	public Optional<Purchase> findPurchaseById(long id) {
		return repo.findById(id);
	}

}
