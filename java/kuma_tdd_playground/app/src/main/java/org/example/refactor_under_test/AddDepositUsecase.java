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
        this.balances.put(userId, this.balances.get(userId) + l);
    }

    public void createAccount(long userId) {
        balances.putIfAbsent(userId, 0L);
    }
}
