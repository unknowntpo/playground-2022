package org.example.refactor_under_test;

import java.util.HashMap;
import java.util.Map;

public class AddDepositUsecase {
    private Map<Long, Long> balances;

    public AddDepositUsecase() {
        this.balances = new HashMap<>(Map.of(1L, 0L, 2L, 0L));
    }

    public long getDeposit(long userId) {
        // TODO: user not exist
        return this.balances.get(userId);
    }

    public void deposit(long userId, long l) {
        this.balances.put(userId, this.balances.get(userId) + l);
    }
}
