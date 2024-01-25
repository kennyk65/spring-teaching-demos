package com.example.factory;


public class ObjectFactory {

    private ObjectFactory() {}

    public static AccountRepository getAccountRepository() {
        return new AccountRepository();
    }

    public static TransferRepository getTransferRepository() {
        return new TransferRepository();
    }

    // public static TransferService getTransferService() {
    //     return new TransferService();
    // }
}
