package org.example.refactor_under_test;

public class AddDepositUsecase {
    private final BankAccountRepository bankAccountRepository;

    public AddDepositUsecase(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public AddDepositUsecase() {
        bankAccountRepository = new BankAccountRepository();
    }

    public long getDeposit(long userId) throws UserNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findBankAccount(userId).orElseThrow(UserNotFoundException::new);
        return bankAccount.getBalance();
    }

    public void deposit(long userId, long l) throws UserNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findBankAccount(userId).orElseThrow(UserNotFoundException::new);

        long newBalance = bankAccount.getBalance() + l;
        bankAccount.setBalance(newBalance);

        bankAccountRepository.save(userId, bankAccount);
    }

    public void createAccount(long userId) {
        bankAccountRepository.createAccount(userId);
    }
}
