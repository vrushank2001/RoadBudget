package dbModule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBSetup {

    private static final String DB_NAME = "RoadBudget";
    private static final String CREATE_DATABASE_SQL = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
    private static final String USE_DATABASE_SQL = "USE " + DB_NAME;

    private static final String CREATE_ACCOUNTS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Accounts ("
            + "username VARCHAR(255) PRIMARY KEY,"
            + "password VARCHAR(255) NOT NULL,"
            + "salt VARCHAR(255) NOT NULL)";

    private static final String CREATE_CARS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Cars ("
            + "carId INT AUTO_INCREMENT PRIMARY KEY,"
            + "carName VARCHAR(255) NOT NULL,"
            + "owner VARCHAR(255),"
            + "FOREIGN KEY (owner) REFERENCES Accounts(username) ON DELETE CASCADE)";

    private static final String CREATE_EXPENSES_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Expenses ("
            + "expenseId INT AUTO_INCREMENT PRIMARY KEY,"
            + "expenseType ENUM('Fuel', 'Maintenance', 'Insurance', 'Other') NOT NULL,"
			+ "amount DECIMAL(10, 2) NOT NULL,"
			+ "date DATE NOT NULL,"
            + "description VARCHAR(255),"
            + "carId INT,"
            + "owner VARCHAR(45),"
			+ "FOREIGN KEY (owner) REFERENCES Accounts(username) ON DELETE CASCADE,"
            + "FOREIGN KEY (carId) REFERENCES Cars(carId) ON DELETE CASCADE)";

    public static void setupDatabase() {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement createDatabaseStatement = connection.prepareStatement(CREATE_DATABASE_SQL);
             PreparedStatement useDatabaseStatement = connection.prepareStatement(USE_DATABASE_SQL);
             PreparedStatement createAccountsTableStatement = connection.prepareStatement(CREATE_ACCOUNTS_TABLE_SQL);
             PreparedStatement createCarsTableStatement = connection.prepareStatement(CREATE_CARS_TABLE_SQL);
             PreparedStatement createExpensesTableStatement = connection.prepareStatement(CREATE_EXPENSES_TABLE_SQL)) {

            createDatabaseStatement.executeUpdate();
            useDatabaseStatement.executeUpdate();

            createAccountsTableStatement.executeUpdate();
            createCarsTableStatement.executeUpdate();
            createExpensesTableStatement.executeUpdate();

            System.out.println("RoadBudget database setup completed successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        setupDatabase();
    }
}
