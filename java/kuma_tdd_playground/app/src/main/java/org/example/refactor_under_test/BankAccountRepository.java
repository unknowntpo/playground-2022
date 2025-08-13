package org.example.refactor_under_test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BankAccountRepository {
    final Map<Long, Long> balances;

    public BankAccountRepository() {
        this.balances = new HashMap<>();
    }

    public Optional<BankAccount> findBankAccount(long userId) {
        if (!this.balances.containsKey(userId)) {
            return Optional.empty();
        }

        long deposit = this.balances.get(userId);

        BankAccount account = new BankAccount();
        account.setBalance(deposit);
        return Optional.of(account);
    }

    public void createAccount(long userId) {
        balances.putIfAbsent(userId, 0L);
    }

    void save(long userId, BankAccount account) {
        balances.put(userId, account.getBalance());
    }
}
