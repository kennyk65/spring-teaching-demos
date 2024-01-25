package com.company.di;

public class TransferService {

    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

//    String accountNumber;

    public TransferService(AccountRepository ar, TransferRepository tr) {
        accountRepository = ar;
        transferRepository = tr;
    }

    // ... 

    public void process(String accountNUmber) {
        // do something with the acount number
    }
}
