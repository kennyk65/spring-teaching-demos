package com.webage.dao;

import java.util.Optional;

import com.webage.domain.Purchase;

public interface ResourceServerDao {

    Iterable<Purchase> findAllPurchases(String jwt);
    Optional<Purchase> findPurchaseById(String jwt, long id);

}
