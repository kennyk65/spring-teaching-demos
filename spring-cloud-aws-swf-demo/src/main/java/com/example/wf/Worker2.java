package com.example.wf;

import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.model.ActivityType;

@Component
public class Worker2 extends Worker {

	@Override
	protected String doTheWork(String input) {
	    return input + ", The man with the foolish grin is keeping perfectly still...";
	}

	@Override
	protected ActivityType getActivityType() {
		return new ActivityType().withName(Utility.ACTIVITY_2).withVersion(Utility.ACTIVITY_VERSION);
	}
	
}
