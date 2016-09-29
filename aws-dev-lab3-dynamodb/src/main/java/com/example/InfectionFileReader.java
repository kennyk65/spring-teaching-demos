package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

//	There really wasn't a good way to use Spring's Resource abstraction here.  
//	The S3 object is in a specific region, and although you can access an S3 
//	object as a resource it complained that you had to access it by a specific endpoint.

//	STATEFUL
@Component
@Scope("prototype")
public class InfectionFileReader {
	
	//	The AmazonS3 client is already setup for us by Spring Cloud AWS,
	//	All we have to do is inject and use:
	@Autowired AmazonS3 s3;
    
    
	private BufferedReader br;
	
	public Infection readInfection(String bucket, String key) {

		try {
			//	Open file if needed:
			if( br == null ) {
		        S3Object infectionsDataObject = s3.getObject(bucket, key);
				br = new BufferedReader(new InputStreamReader(infectionsDataObject.getObjectContent()));
			}
	
			//	Read the next line:
			String line = br.readLine();
	
			//	Close the file / return null when done.
			if (line == null) {
				br.close();
				br = null;
				return null;
			}
	
			//	Parse input into array:
			String[] infectionsDataAttrValues = line.split(",");	
	
			// Skip first line because it has the attribute names only.
	        if (infectionsDataAttrValues[0].equals("PatientId")) {
	        	return readInfection(bucket, key);
	        }
	        
	        //	Instantiate and return the Infections object based on the CSV data;
	        return new Infection(infectionsDataAttrValues[0],infectionsDataAttrValues[1],infectionsDataAttrValues[2]);
		} catch (Exception e) {
			System.out.println("Error occurred while reading infections data: " + e.getLocalizedMessage());
		}
		return null;
	}
	
}
