package com.example;

import org.junit.jupiter.api.Test;

import com.example.di.AccountRepository;
import com.example.di.TransferRepository;
import com.example.di.TransferService;

public class TestTransferService {

    @Test
    public void testTransferService() {
        AccountRepository ar = new AccountRepository();
        TransferRepository tr = new TransferRepository();
        (new TransferService(ar, tr)).someMethod();
    }
}
