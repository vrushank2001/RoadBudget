package models;

import java.util.Date;

public class ExpenseRecord {
	private int id;
    private String type;
    private String description;
    private double amount;
    private Date date;
    private int carId;
    private String owner;
    private String carName;
    

    public ExpenseRecord(String type, String description, double amount, Date date) {
    	this.type = type;
        this.type = type;
        this.description = description;
        this.amount = amount;
		this.date = date;
    }

    public String getType() { return type; }
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