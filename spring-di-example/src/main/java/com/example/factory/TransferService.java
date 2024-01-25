package com.example.factory;

public class TransferService {

    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

    public TransferService() {
        accountRepository = ObjectFactory.getAccountRepository();
        transferRepository = ObjectFactory.getTransferRepository();
    }

    public void someMethod() {
        accountRepository.someMethod();
    }
}
