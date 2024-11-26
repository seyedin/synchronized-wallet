package org.example.model;

import lombok.Data;
import org.example.exception.InsufficientFundsException;

@Data
public class Wallet {

    private int id;
    private int balance;

    public Wallet(int id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public synchronized void charge(int amount) {
        balance += amount;
    }

    public synchronized void withdraw(int amount) throws InsufficientFundsException {
        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

    public synchronized int getBalance() {
        return balance;
    }
}