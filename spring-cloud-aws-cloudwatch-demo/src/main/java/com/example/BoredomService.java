package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoredomService {

	@Autowired MonitoringService monitoringService;
	
	public String assessBoredomLevel(Long level) {
		
		monitoringService.logBoredomLevel(level);
		
		int i = level.intValue();
		if ( i > 95 ) return "Eeeesh!!  You need coffee!";
		if ( i > 85 ) return "What, application monitoring doesn't interest you?";
		if ( i > 75 ) return "Get enough sleep last night?";
		if ( i > 65 ) return "Tell the instructor to liven things up a bit.";
		if ( i > 55 ) return "But this is monitoring man!  Don't you live for this stuff?";
		if ( i > 45 ) return "Ok";
		if ( i > 35 ) return "Normal.  ";
		if ( i > 25 ) return "Not bad";
		if ( i > 15 ) return "Good, you have a disciplined mind.";
		if ( i > 5 ) return "Nerd.";
		
		return "Don't try skydiving, the thrill may kill you.";
	}
}
