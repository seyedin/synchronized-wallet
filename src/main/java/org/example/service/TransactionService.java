package org.example.service;

import org.example.exception.InsufficientFundsException;
import org.example.model.Wallet;
import org.example.repository.WalletRepositoryImpl;

import java.sql.SQLException;

public class TransactionService {
    private final WalletRepositoryImpl walletRepository = new WalletRepositoryImpl();

    public void charge(Wallet wallet, int amount) throws SQLException {
        synchronized (wallet) {
            wallet.charge(amount);
            walletRepository.updateBalance(wallet.getId(), wallet.getBalance());
            System.out.println("Charged: " + amount + ", New Balance: " + wallet.getBalance());
        }
    }

    public void withdraw(Wallet wallet, int amount) throws InsufficientFundsException, SQLException {
        synchronized (wallet) {
            wallet.withdraw(amount);
            walletRepository.updateBalance(wallet.getId(), wallet.getBalance());
            System.out.println("Withdrawn: " + amount + ", New Balance: " + wallet.getBalance());
        }
    }
}