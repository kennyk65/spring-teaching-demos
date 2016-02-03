package com.demo.ex;


public class Car {

	private int numberOfDoors = 0;
	private boolean hybrid = false;
	private String color = null;
	private double engineDisplacement = 0;
	
	
	
	public int getNumberOfDoors() {
		return numberOfDoors;
	}
	public void setNumberOfDoors(int numberOfDoors) {
		this.numberOfDoors = numberOfDoors;
	}
	public boolean isHybrid() {
		return hybrid;
	}
	public void setHybrid(boolean hybrid) {
		this.hybrid = hybrid;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public double getEngineDisplacement() {
		return engineDisplacement;
	}
	public void setEngineDisplacement(double engineDisplacement) {
		this.engineDisplacement = engineDisplacement;
	}
	@Override
	public String toString() {
		return "Car [numberOfDoors=" + numberOfDoors + ", hybrid=" + hybrid
				+ ", color=" + color + ", engineDisplacement="
				+ engineDisplacement + "]";
	}
	
	
}
