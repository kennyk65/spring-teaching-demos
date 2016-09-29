package com.example;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfectionService {

	@Autowired InfectionFileReader reader;
	@Autowired InfectionRepository dao;		//	Generated dynamically by Spring Data.
	
	public void loadInfectionsFromS3() throws Exception {

        System.out.println("Loading infections from S3 object...");
		
		List<Infection> infectionList = new ArrayList<>();
		Infection infection = reader.readInfection(Utils.S3_BUCKET_NAME, Utils.INFECTIONS_DATA_FILE_KEY);
		while ( infection != null) {
			infectionList.add(infection);
			infection = reader.readInfection(Utils.S3_BUCKET_NAME, Utils.INFECTIONS_DATA_FILE_KEY);
		}

		System.out.println("Saving infections to DynamoDB...");
		dao.save(infectionList);
	}
	
}
