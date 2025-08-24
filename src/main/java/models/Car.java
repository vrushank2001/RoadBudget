package models;

public class Car {
    private int carId;
    private String carName;

    public Car(int carId, String carName) {
        this.carId = carId;
        this.carName = carName;
    }

    public int getCarId() { return carId; }
    public String getCarName() { return carName; }
    
	public void setCarId(int carId) { this.carId = carId; }
	public void setCarName(String carName) { this.carName = carName; }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return carId == car.carId && carName.equals(car.carName); // equality by carId and carName
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(carId);
    }
    
    @Override
    public String toString() {
        return carName;
    }
}