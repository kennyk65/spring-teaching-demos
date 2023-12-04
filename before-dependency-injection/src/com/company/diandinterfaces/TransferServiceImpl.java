package com.company.diandinterfaces;

public class TransferServiceImpl implements TransferService {

    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

    public TransferService(AccountRepository ar, TransferRepository tr) {
        accountRepository = ar;
        transferRepository = tr;
    }

    // ... 
}
