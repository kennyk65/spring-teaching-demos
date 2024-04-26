package com.webage.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.webage.domain.Purchase;
import com.webage.repository.PurchaseRepository;

@RestController
public class PurchaseAPI {
    
    @Autowired  PurchaseRepository repo;

    @GetMapping("/purchases")
    public Iterable<Purchase> getAll() {
        return repo.findAll();
    }
    
    @GetMapping("/purchases/{id}")
    public Optional<Purchase> getPurchase(@PathVariable long id) {
        return repo.findById(id);
    }

    @GetMapping("/")
    public String health() {
        return "Resource Server is running.";
    }
}

