package com.example.di;

public class TransferService {

    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

    public TransferService(AccountRepository ar, TransferRepository tr) {
        accountRepository = ar;
        transferRepository = tr;
    }

    public void someMethod() {
        accountRepository.someMethod();
    }
}
