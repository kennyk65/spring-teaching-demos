package com.example.wf;

import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.model.ActivityType;

@Component
public class Worker1 extends Worker {

	@Override
	protected String doTheWork(String input) {
	    return input + ", Alone on a hill";
	}

	@Override
	protected ActivityType getActivityType() {
		return new ActivityType().withName(Utility.ACTIVITY_1).withVersion(Utility.ACTIVITY_VERSION);
	}

	
}
