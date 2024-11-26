package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.model.Wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletRepositoryImpl {

    public boolean updateBalance(int walletId, int balance) throws SQLException {
        String sql = """
                UPDATE wallets SET balance = ? WHERE id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, balance);
            preparedStatement.setInt(2, walletId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // Rethrow the exception to let the caller know something went wrong
        }
    }

    public Wallet findById(int walletId) throws SQLException {
        String sql = """
                SELECT * FROM wallets WHERE id = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, walletId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int balance = resultSet.getInt("balance");
                    return new Wallet(walletId, balance);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }
}