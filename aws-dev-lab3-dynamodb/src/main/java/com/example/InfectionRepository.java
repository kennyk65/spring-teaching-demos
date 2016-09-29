package com.example;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;

//@EnableScan //	Allows table scans, not just queries by the keys.
public interface InfectionRepository extends CrudRepository<Infection,String> {

	@EnableScan	// I shouldn't have to do this as I have GSI and city is a @DynamoDBIndexHashKey
	List<Infection> findByCity(String city);
}
