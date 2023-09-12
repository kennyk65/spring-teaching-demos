package com.example;

public class TransferService {

    private AccountRepository repo;

    public TransferService(AccountRepository repo) {
        this.repo = repo;
    }

    public void someMethod() {
        repo.someMethod();
    }
}
