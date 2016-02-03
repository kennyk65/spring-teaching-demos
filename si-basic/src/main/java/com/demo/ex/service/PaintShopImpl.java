package com.demo.ex.service;

import com.demo.ex.Car;

public class PaintShopImpl implements PaintShop {

	public Car paintCar(Car car) {
		
		double d = Math.random();
		if(d < 0.3){
			car.setColor("Blue");
		} else if ( d < .6 ) {
			car.setColor("Red");
		} else {
			car.setColor("White");
		}
		
		return car;
	}

}
