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
        return bankAccountRepository.findBankAccount(userId).getBalance();
    }

    public void deposit(long userId, long l) throws UserNotFoundException {
        BankAccount account = bankAccountRepository.findBankAccount(userId);

        long newBalance = account.getBalance() + l;
        account.setBalance(newBalance);

        bankAccountRepository.save(userId, account);
    }

    public void createAccount(long userId) {
        bankAccountRepository.createAccount(userId);
    }
}
