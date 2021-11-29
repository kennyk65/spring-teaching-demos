package com.company.factory;

public class TransferService {

    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

    public TransferService() {
        accountRepository = ObjectFactory.makeAccountRepository();
        transferRepository = ObjectFactory.makeTransferRepository();
    }

    // ... 
}
