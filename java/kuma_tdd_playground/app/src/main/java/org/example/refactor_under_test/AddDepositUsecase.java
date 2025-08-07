package org.example.refactor_under_test;

import java.util.HashMap;
import java.util.Map;

public class AddDepositUsecase {
    private final Map<Long, Long> balances;

    public AddDepositUsecase() {
        this.balances = new HashMap<>();
    }

    public long getDeposit(long userId) throws UserNotFoundException {
        if (!this.balances.containsKey(userId)) {
            throw new UserNotFoundException();
        }
        return this.balances.get(userId);
    }

    public void deposit(long userId, long l) throws UserNotFoundException {
        if (!this.balances.containsKey(userId)) {
            throw new UserNotFoundException();
        }

        var currentBalance = this.balances.get(userId);
        long newBalance = currentBalance + l;

        this.balances.put(userId, newBalance);
    }

    public void createAccount(long userId) {
        balances.putIfAbsent(userId, 0L);
    }
}
