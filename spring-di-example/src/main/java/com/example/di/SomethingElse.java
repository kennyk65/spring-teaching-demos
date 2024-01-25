package com.example.di;

public class SomethingElse {
    
    public static void main(String[] args) {
    
        AccountRepository accountRepository = new AccountRepository();
        TransferRepository transferRepository = new TransferRepository();

        TransferService transferService 
            = new TransferService(accountRepository,transferRepository);
    
        transferService.someMethod();    
    }

}
