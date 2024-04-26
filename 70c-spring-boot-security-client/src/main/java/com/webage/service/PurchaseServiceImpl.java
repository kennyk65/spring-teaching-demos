package com.webage.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webage.dao.AuthorizationServerDao;
import com.webage.dao.ResourceServerDao;
import com.webage.domain.Purchase;


@Service
public class PurchaseServiceImpl implements PurchaseService {

	@Autowired AuthorizationServerDao authorizationServiceClient;
	@Autowired ResourceServerDao resourceServerClient;

	public Iterable<Purchase> findAllPurchases() {
		String jwt = authorizationServiceClient.getJwt();
		return resourceServerClient.findAllPurchases(jwt);
	}

	public Optional<Purchase> findPurchaseById(long id) {
		String jwt = authorizationServiceClient.getJwt();
		return resourceServerClient.findPurchaseById(jwt,id);
	}

}
