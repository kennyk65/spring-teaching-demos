package com.demo.ex.service;

import com.demo.ex.Car;

public class BodyShopImpl implements BodyShop {

	public Car installDoors(Car car) {
		
		double d = Math.random();
		if(d < 0.3){
			car.setNumberOfDoors(2);
		} else if ( d < .6 ) {
			car.setNumberOfDoors(3);
		} else if ( d < .8 ) {
			car.setNumberOfDoors(4);
		} else {
			car.setNumberOfDoors(5);
		}
		
		return car;
	}

}
