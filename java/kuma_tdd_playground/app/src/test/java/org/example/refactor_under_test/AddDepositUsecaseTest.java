package org.example.refactor_under_test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddDepositUsecaseTest {


    @Test
    void test_user_exist() throws UserNotFoundException {
        var usecase = new AddDepositUsecase();
        var userId = 1L;

        assertThrows(UserNotFoundException.class, ()-> usecase.getDeposit(userId));
        assertThrows(UserNotFoundException.class, ()-> usecase.deposit(userId, 10L));

        usecase.createAccount(userId);
        // create twice has no effect
        assertDoesNotThrow(() -> usecase.createAccount(userId));

        assertEquals(0, usecase.getDeposit(userId));
        usecase.deposit(userId, 100);
        assertEquals(100, usecase.getDeposit(userId));
        usecase.deposit(userId, 200);
        assertEquals(300, usecase.getDeposit(userId));
    }

    @Test
    void test_two_users() throws UserNotFoundException {
        var userId0 = 1L;
        var userId1 = 2L;
        var usecase = new AddDepositUsecase();

        usecase.createAccount(userId0);
        usecase.createAccount(userId1);
        assertEquals(0, usecase.getDeposit(userId0));
        assertEquals(0, usecase.getDeposit(userId1));

        usecase.deposit(userId0, 100);
        assertEquals(100, usecase.getDeposit(userId0));
        usecase.deposit(userId1, 200);
        assertEquals(200, usecase.getDeposit(userId1));
    }
}


