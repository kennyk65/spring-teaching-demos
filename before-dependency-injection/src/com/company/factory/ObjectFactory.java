package com.company.factory;

public class ObjectFactory {

    public static TransferRepository makeTransferRepository() {
        return new TransferRepository();
    }

    public static AccountRepository makeAccountRepository() {
        return new AccountRepository();
    }

}
