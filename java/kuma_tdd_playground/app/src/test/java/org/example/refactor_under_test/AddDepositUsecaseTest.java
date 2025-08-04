package org.example.refactor_under_test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddDepositUsecaseTest {


    @Test
    void test_user_exist() {
        var usecase = new AddDepositUsecase();
        var userId = 1L;
        assertEquals(0, usecase.getDeposit(userId));
        usecase.deposit(1, 100);
        assertEquals(100, usecase.getDeposit(userId));
        usecase.deposit(1, 200);
        assertEquals(300, usecase.getDeposit(userId));
    }

    @Test
    void test_two_users() {
        var userId0 = 1L;
        var userId1 = 2L;
        var usecase = new AddDepositUsecase();
        assertEquals(0, usecase.getDeposit(userId0));
        assertEquals(0, usecase.getDeposit(userId1));
        usecase.deposit(1, 100);
        assertEquals(100, usecase.getDeposit(userId0));
        usecase.deposit(userId1, 200);
        assertEquals(200, usecase.getDeposit(userId1));
    }
}


