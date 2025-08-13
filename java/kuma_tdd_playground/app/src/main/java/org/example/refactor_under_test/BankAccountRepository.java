package org.example.refactor_under_test;

import java.util.HashMap;
import java.util.Map;

public class BankAccountRepository {
    final Map<Long, Long> balances;

    public BankAccountRepository() {
        this.balances = new HashMap<>();
    }

    public BankAccount findBankAccount(long userId) throws UserNotFoundException {
        if (!this.balances.containsKey(userId)) {
            throw new UserNotFoundException();
        }
        long deposit = this.balances.get(userId);

        BankAccount account = new BankAccount();
        account.setBalance(deposit);
        return account;
    }

    public void createAccount(long userId) {
        balances.putIfAbsent(userId, 0L);
    }

    void save(long userId, BankAccount account) {
        balances.put(userId, account.getBalance());
    }
}
