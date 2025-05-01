package models;

import java.util.Date;

public class ExpenseRecord {
	private int id;
	
	public enum ExpenseType {
		
		FUEL("Fuel"),
		MAINTENANCE("Maintenance"),
		CAR_INSURANCE("Car Insurance"),
		TENANT_INSURANCE("Tenant Insurance"),
		OTHER("Other");

		private final String type;

	    ExpenseType(String displayName) {
	        this.type = displayName;
	    }

	    public String getType() {
	        return type;
	    }
	    
	    public String toString() {
		    return getType().toString();
	    }
	    
	    public static ExpenseType fromDisplayName(String displayName) {
	        for (ExpenseType type : ExpenseType.values()) {
	            if (type.getType().equalsIgnoreCase(displayName)) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("No ExpenseType with display name: " + displayName);
	    }
	    
	    public static String[] getAllTypes() {
	        String[] displayNames = new String[ExpenseType.values().length];
	        for (int i = 0; i < ExpenseType.values().length; i++) {
	            displayNames[i] = ExpenseType.values()[i].getType();
	        }
	        return displayNames;
	    }
	} 
	
	private ExpenseType type;
    private String description;
    private double amount;
    private Date date;
    private int carId;
    private String owner;
    private String carName;
    

    public ExpenseRecord(ExpenseType type, String description, double amount, Date date) {
    	this.type = type;
        this.description = description;
        this.amount = amount;
		this.date = date;
    }

    public ExpenseType getType() { return type; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
	public Date getDate() { return date; }
	public int getId() { return id; }
	public int getCarId() { return carId; }
	public String getOwner() { return owner; }
	public String getCarName() { return carName; }
	
	public void setId(int id) { this.id = id; }
	public void setCarId(int carId) { this.carId = carId; }
	public void setOwner(String owner) { this.owner = owner; }
	public void setCarName(String carName) { this.carName = carName; }
}