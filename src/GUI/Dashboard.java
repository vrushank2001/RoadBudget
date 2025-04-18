package GUI;

import dbModule.DBOperations;
import models.ExpenseRecord;
import models.Car;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class Dashboard extends JFrame {
    private JComboBox<Object> carDropdown;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private String ownerUsername;
    private List<Car> userCars;

    public Dashboard(String username) {
        this.ownerUsername = username;
        setTitle("Dashboard - RoadBudget");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel(new FlowLayout());
        carDropdown = new JComboBox<>();
        carDropdown.addItem("All Cars");
        userCars = DBOperations.getCarsForUser(username);
        for (Car car : userCars) {
            carDropdown.addItem(car);
        }
        carDropdown.addActionListener(e -> loadExpenses());

        JButton addCarBtn = new JButton("Add Car");
        JButton deleteCarBtn = new JButton("Delete Car");
        JButton addExpenseBtn = new JButton("Add Expense");
        JButton deleteExpenseBtn = new JButton("Delete Expense");

        addCarBtn.addActionListener(e -> openAddCarDialog());
        deleteCarBtn.addActionListener(e -> openDeleteCarDialog());
        addExpenseBtn.addActionListener(e -> openAddExpenseDialog());
		deleteExpenseBtn.addActionListener(e -> openDeleteExpenseDialog());

		topPanel.add(carDropdown);
		topPanel.add(addCarBtn);
		topPanel.add(deleteCarBtn);
		topPanel.add(addExpenseBtn);
		topPanel.add(deleteExpenseBtn);

        tableModel = new DefaultTableModel(new Object[]{"ExpenseId", "Type", "Amount", "Date", "Description", "Car"}, 0);
        expenseTable = new JTable(tableModel);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        totalLabel = new JLabel("Total: $0.0");
        JButton logoutButton = new JButton("Logout");
        
        bottomPanel.add(totalLabel);
        bottomPanel.add(logoutButton);
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new MainPage().setVisible(true);
            	dispose();
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(expenseTable), BorderLayout.CENTER);
        
        add(bottomPanel, BorderLayout.SOUTH);

        loadExpenses();
        setVisible(true);
    }

    private void loadExpenses() {
        tableModel.setRowCount(0);
        Object selected = carDropdown.getSelectedItem();
        Integer filterCarId = null;
        if (selected instanceof Car) {
            filterCarId = ((Car) selected).getCarId();
        }
        List<ExpenseRecord> expenses = DBOperations.getExpensesForUser(ownerUsername, filterCarId);
        for (ExpenseRecord e : expenses) {
            tableModel.addRow(new Object[]{e.getId(), e.getType(), e.getAmount(), e.getDate(), e.getDescription(), e.getCarName()});
        } 
        totalLabel.setText("Total: $" + DBOperations.getTotalExpensesForUser(ownerUsername));
    }

    private void openAddCarDialog() {
        String carName = JOptionPane.showInputDialog(this, "Enter car name:");
        if (carName != null && !carName.trim().isEmpty()) {
            DBOperations.insertCar(carName, ownerUsername);
            int totalCarsofUser = DBOperations.getCarsForUser(ownerUsername).size();
            userCars.add(new Car(DBOperations.getCarsForUser(ownerUsername).get(totalCarsofUser - 1).getCarId(), carName));
            carDropdown.addItem(new Car(DBOperations.getCarsForUser(ownerUsername).get(totalCarsofUser - 1).getCarId(), carName));
            loadExpenses();
        }
    }
    
    private void openDeleteCarDialog() {
		Object selected = carDropdown.getSelectedItem();
		if (selected instanceof Car) {
			int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this car (" + selected.toString() + ") ?", "Delete Car", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				DBOperations.deleteCar(((Car) selected).getCarId());
				carDropdown.removeItem(selected);
				loadExpenses();
			}
			else {
				JOptionPane.showMessageDialog(this, "Select car to delete from the dropdown.");
			}
		}
	}

    private void openAddExpenseDialog() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        String[] types = {"Fuel", "Repair", "Insurance", "Other"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        JTextField descriptionField = new JTextField();
        JTextField amountField = new JTextField();
        JDateChooser dateChooser = new JDateChooser();

        JComboBox<Car> carBox = new JComboBox<>();
        for (Car car : userCars) carBox.addItem(car);

        panel.add(new JLabel("Type:")); panel.add(typeBox);
        panel.add(new JLabel("Description:")); panel.add(descriptionField);
        panel.add(new JLabel("Amount:")); panel.add(amountField);
        panel.add(new JLabel("Date:")); panel.add(dateChooser);
        panel.add(new JLabel("Car:")); panel.add(carBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String type = (String) typeBox.getSelectedItem();
                String desc = descriptionField.getText();
                
                //round to 2 decimal places
                double amount = Double.parseDouble(amountField.getText());
				amount = Math.round(amount * 100.0) / 100.0;
				
                Date date = dateChooser.getDate();
                Car selectedCar = (Car) carBox.getSelectedItem();

                ExpenseRecord record = new ExpenseRecord(type, desc, amount, date);
                record.setCarId(selectedCar.getCarId());
                DBOperations.insertExpense(record, ownerUsername);
                loadExpenses();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        }
    }
    
    private void openDeleteExpenseDialog() {
	    int selectedRow = expenseTable.getSelectedRow();
	    if (selectedRow != -1) {
	        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this expense?", "Delete Expense", JOptionPane.YES_NO_OPTION);
	        if (result == JOptionPane.YES_OPTION) {
	            int expenseId = (int) expenseTable.getValueAt(selectedRow, 0);
	            DBOperations.deleteExpense(expenseId);
	            loadExpenses();
	        }
	    }
	    else {
	    	JOptionPane.showMessageDialog(this, "Please select an expense to delete from the table.");
	    }
    }
    
    public static void main(String[] args) {
    	// Create instance of MainPage to start the application
        new Dashboard("vrushank");
    }
}