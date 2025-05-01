package dbModule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Car;
import models.ExpenseRecord;
import models.ExpenseRecord.ExpenseType;
import utilitiesModule.SecurityUtils;


public class DBOperations {
	
	
	public static void insertAccount(String username, String password, String salt, String email) {
	    try (Connection connection = DBConnector.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(
	                 "INSERT INTO Accounts (username, password, salt, email) VALUES (?, ?, ?, ?)")) {

	        preparedStatement.setString(1, username);
	        preparedStatement.setString(2, password);
	        preparedStatement.setString(3, salt);
	        preparedStatement.setString(4, email);
	        preparedStatement.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static boolean authenticateUser(String username, String inputPassword) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT password, salt FROM Accounts WHERE username = ?")) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedHash = resultSet.getString("password");
                String salt = resultSet.getString("salt");

                String inputHash = SecurityUtils.hashPassword(inputPassword, salt);
                return storedHash.equals(inputHash);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // authentication failed
    }
	
	public static boolean userExists(String usernameOrEmail) {
	    try (Connection connection = DBConnector.getConnection();
	         PreparedStatement statement = connection.prepareStatement("SELECT username FROM Accounts WHERE username = ? OR email = ?")) {
	        statement.setString(1, usernameOrEmail);
	        statement.setString(2, usernameOrEmail);
	        ResultSet rs = statement.executeQuery();
	        return rs.next();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static boolean emailExists(String email) {
		try (Connection connection = DBConnector.getConnection();
			 PreparedStatement statement = connection.prepareStatement("SELECT email FROM Accounts WHERE email = ?")) {
			statement.setString(1, email);
			ResultSet rs = statement.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getUserEmail(String usernameOrEmail) {
		try (Connection connection = DBConnector.getConnection();
			 PreparedStatement statement = connection.prepareStatement("SELECT email FROM Accounts WHERE username = ? OR email = ?")) {
			statement.setString(1, usernameOrEmail);
			statement.setString(2, usernameOrEmail);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getString("email");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean updateUserPassword(String emailOrUsername, String hashedPassword, String salt) {
		try (Connection connection = DBConnector.getConnection();
			 PreparedStatement statement = connection.prepareStatement("UPDATE Accounts SET password = ?, salt = ? WHERE email = ? OR username = ?")) {
			statement.setString(1, hashedPassword);
			statement.setString(2, salt);
			statement.setString(3, emailOrUsername);
			statement.setString(4, emailOrUsername);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	public static void insertCar(String carName, String ownerUsername) {
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Cars (carName, owner) VALUES (?, ?)");) {
            stmt.setString(1, carName);
            stmt.setString(2, ownerUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public static void deleteCar(int carId) {
		try (Connection conn = DBConnector.getConnection();
			 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Cars WHERE carId = ?");) {
			stmt.setInt(1, carId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    public static void insertExpense(ExpenseRecord record, String ownerUsername) {
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Expenses (expenseType, description, amount, date, carId, owner) VALUES (?, ?, ?, ?, ?, ?)");) {
            stmt.setString(1, record.getType().toString());
            stmt.setString(2, record.getDescription());
            stmt.setDouble(3, record.getAmount());
            stmt.setDate(4, new java.sql.Date(record.getDate().getTime()));
            stmt.setInt(5, record.getCarId());
            stmt.setString(6, ownerUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteExpense(int expenseId) {
		try (Connection conn = DBConnector.getConnection();
			 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Expenses WHERE expenseId = ?");) {
			stmt.setInt(1, expenseId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public static List<Car> getCarsForUser(String username) {
        List<Car> cars = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT carId, carName FROM Cars WHERE owner = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cars.add(new Car(rs.getInt("carId"), rs.getString("carName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static List<ExpenseRecord> getExpensesForUser(String username, Integer carIdFilter) {
        List<ExpenseRecord> records = new ArrayList<>();
        String sql = "SELECT e.*, c.carName FROM Expenses e JOIN Cars c ON e.carId = c.carId WHERE c.owner = ?";
        if (carIdFilter != null) {
            sql += " AND e.carId = ?";
        }

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            if (carIdFilter != null) {
                stmt.setInt(2, carIdFilter);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ExpenseRecord record = new ExpenseRecord(
                        ExpenseType.fromDisplayName(rs.getString("expenseType")),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getDate("date")
                );
                record.setId(rs.getInt("expenseId"));
                record.setCarId(rs.getInt("carId"));
                record.setOwner(username);
                record.setCarName(rs.getString("carName"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static double getTotalExpensesAmountForUser(String username) {
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT SUM(amount) FROM Expenses e JOIN Cars c ON e.carId = c.carId WHERE c.owner = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
