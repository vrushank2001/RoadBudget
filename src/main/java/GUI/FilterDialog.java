package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dbModule.DBOperations;
import models.Car;
import models.ExpenseRecord;
import models.ExpenseRecord.ExpenseType;

public class FilterDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JCheckBox[] expenseTypeCheckboxes;
	private JComboBox<String> carDropdown;
	private String ownerUsername;
	private Dashboard parent;


	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public FilterDialog(String username, Dashboard parent) {
		JDialog filterDialog = new JDialog(this, "Filter Expenses", true);
	    filterDialog.setSize(300, 400);
	    filterDialog.setLocationRelativeTo(this);
	    filterDialog.setLayout(new GridBagLayout());
	    
	    this.ownerUsername = username;
	    this.parent = parent;

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    // Expense Type Checkboxes
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    filterDialog.add(new JLabel("Select Expense Types:"), gbc);

	    gbc.gridwidth = 1;
	    gbc.gridy++;
	    JPanel checkboxPanel = new JPanel(new GridLayout(0, 1));
	    expenseTypeCheckboxes = Arrays.stream(ExpenseType.getAllTypes())
	        .map(type -> new JCheckBox(type.toString()))
	        .toArray(JCheckBox[]::new);
	    for (JCheckBox cb : expenseTypeCheckboxes) checkboxPanel.add(cb);

	    gbc.gridx = 0;
	    gbc.gridwidth = 2;
	    filterDialog.add(checkboxPanel, gbc);

	    // Car Dropdown
	    gbc.gridy++;
	    gbc.gridwidth = 2;
	    filterDialog.add(new JLabel("Select Car:"), gbc);

	    gbc.gridy++;
	    this.carDropdown = new JComboBox<>();
	    this.carDropdown.addItem("All Cars");
	    for (Car car : DBOperations.getCarsForUser(ownerUsername)) {
	        this.carDropdown.addItem(car.getCarName());
	    }
	    filterDialog.add(carDropdown, gbc);

	    // OK Button
	    gbc.gridy++;
	    gbc.gridwidth = 2;
	    JButton okButton = new JButton("OK");
	    filterDialog.add(okButton, gbc);

	    okButton.addActionListener(event -> {
	        Set<ExpenseType> selectedTypes = new HashSet<>();
	        for (JCheckBox cb : expenseTypeCheckboxes) {
	            if (cb.isSelected()) selectedTypes.add(ExpenseType.fromDisplayName(cb.getText()));
	        }

	        String selectedCar = carDropdown.getSelectedItem().toString();
	        filterExpenses(selectedTypes, selectedCar);  // Call the filter function
	        filterDialog.dispose();
	    });

	    filterDialog.setVisible(true);
	}
	
	private void filterExpenses(Set<ExpenseType> selectedTypes, String selectedCar) {
	    List<ExpenseRecord> allExpenses = DBOperations.getExpensesForUser(ownerUsername, null);  // Assuming you already load them

	    List<ExpenseRecord> filtered = allExpenses.stream()
	        .filter(exp -> selectedTypes.contains(exp.getType()))
	        .filter(exp -> selectedCar.equals("All Cars") || exp.getCarName().equals(selectedCar))
	        .collect(Collectors.toList());

	    parent.updateExpenseTable(filtered);

	}

	

}
