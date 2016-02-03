package com.demo.ex.service;

import com.demo.ex.Car;

public class EngineShopImpl implements EngineShop {

	public Car installEngine(Car car) {
		
		//	Randomly choose engine size:
		double d = Math.random();
		if(d < 0.3){
			car.setEngineDisplacement(2);
		} else if ( d < .6 ) {
			car.setEngineDisplacement(3);
		} else {
			car.setEngineDisplacement(4);
		}
		
		//	Randomly make hybrid:
		d = Math.random();
		car.setHybrid( d > .5 );
		
		return car;
	}

}
