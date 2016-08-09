package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.wf.Decider;
import com.example.wf.Starter;
import com.example.wf.Utility;
import com.example.wf.Worker1;
import com.example.wf.Worker2;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class WorkFlowTest {

	@Autowired Utility utility;
	@Autowired Worker1 worker1;
	@Autowired Worker2 worker2;
	@Autowired Decider decider;
	@Autowired Starter starter;
	
	@Test
	public void testwf() throws Exception {
		
		//	Setup:
		utility.setup();

		//	Launch the workflow:
		starter.start();
		
		//	Launch workers:
		worker1.pollForWork();
		worker2.pollForWork();
		decider.pollForDecisions();
		
		
		Thread.sleep(15000);	//	Give it time to finish.
	}
}
