package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * Used to run from the command line.  Processes the incoming args as drug names.
 */
@Service
public class Runner implements CommandLineRunner{

	@Autowired PharmService service;
	
	@Override
	public void run(String... args) throws Exception {
		if (args == null || args.length < 1) {
			System.out.println("Please enter the pharmaceuticals you need information on");
			return;
		}

		for (String s : args) {
			System.out.println( s + ": " + service.getPharmaInfo(s));
		}
		return;
	}

}
