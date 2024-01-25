package com.example.naive;

public class TransferService {

    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

    public TransferService() {
        accountRepository = new AccountRepository();
        transferRepository = new TransferRepository();
    }

    public void someMethod() {
        accountRepository.someMethod();
    }
}
