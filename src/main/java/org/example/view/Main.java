package org.example.view;

import org.example.database.DatabaseConnection;
import org.example.exception.InsufficientFundsException;
import org.example.model.Wallet;
import org.example.service.TransactionService;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Wallet wallet = new Wallet(1, 1000);// مقدار اولیه کیف پول 1000

        TransactionService transactionService = new TransactionService();
        Runnable chargeTask = () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    transactionService.charge(wallet, 100);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable withdrawTask = () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    transactionService.withdraw(wallet, 200);
                } catch (InsufficientFundsException | SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        Thread chargeThread1 = new Thread(chargeTask);
        Thread chargeThread2 = new Thread(chargeTask);
        Thread withdrawThread1 = new Thread(withdrawTask);
        Thread withdrawThread2 = new Thread(withdrawTask);
        chargeThread1.start();
        chargeThread2.start();
        withdrawThread1.start();
        withdrawThread2.start();
        try {
            chargeThread1.join();
            chargeThread2.join();
            withdrawThread1.join();
            withdrawThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // بستن اتصال پایگاه داده
        try {
            DatabaseConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //  محاسبه و بررسی موجودی نهایی
        System.out.println("Final balance: " + wallet.getBalance());
        int expectedBalance = 1000 + (10 * 100) - (10 * 200);


        System.out.println("Expected balance: " + expectedBalance);
        if (wallet.getBalance() != expectedBalance)
            throw new AssertionError("Final balance does not match expected balance");
    }
}