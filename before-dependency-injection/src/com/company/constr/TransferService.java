package com.company.constr;

public class TransferService {

    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

    public TransferService() {
        accountRepository = new AccountRepository();
        transferRepository = new TransferRepository();
    }

    // ... 
}
