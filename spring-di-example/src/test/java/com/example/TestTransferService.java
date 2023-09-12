package com.example;

import org.junit.jupiter.api.Test;

public class TestTransferService {

    @Test
    public void testTransferService() {
        AccountRepository repo = new AccountRepository();
        (new TransferService(repo)).someMethod();
    }
}
