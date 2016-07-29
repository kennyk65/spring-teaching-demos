package com.example;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class KillService {

	@Async
	public void killTheJvm() throws Exception {
		System.out.println("KILLING JVM NOW!");
		System.exit(1);
	}
}
