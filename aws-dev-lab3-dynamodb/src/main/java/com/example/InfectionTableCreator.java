package com.example;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

@Component
public class InfectionTableCreator {

    public static final String INFECTIONS_TABLE_NAME = "Infection";
    public static final String CITY_DATE_INDEX_NAME = "InfectionsByCityDate";
	
	@Autowired DynamoDB dynamoDB;
	
	public void setup() {
		//	Clear out the table (if it exists) and create a fresh one:
		removeInfectionsTableIfExists();
		createInfectionsTable();
		
	}
	
	public void cleanup() {
		//	Clear out the table:
		removeInfectionsTableIfExists();
	}
	
    private void removeInfectionsTableIfExists() {
        try {
            Table table = dynamoDB.getTable(Utils.INFECTIONS_TABLE_NAME);
            System.out.println("Attempting to delete DynamoDB table if one already exists.");
            table.delete();
            table.waitForDelete();
        } catch (ResourceNotFoundException e) {
            System.out.printf("%s table does not exist. Do not need to remove it. \n", Utils.INFECTIONS_TABLE_NAME);
        } catch (InterruptedException ie) {
        }
    }
 
    private void createInfectionsTable() {

        System.out.println("Creating DynamoDB table...");

        //	Table creation need only contain only attributes that are in the key schema.  
        GlobalSecondaryIndex gsi = new GlobalSecondaryIndex() // @Del
                .withIndexName(CITY_DATE_INDEX_NAME) // @Del
                .withKeySchema(new KeySchemaElement("city", KeyType.HASH), new KeySchemaElement("date", KeyType.RANGE)) // @Del
                .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L)) // @Del
                .withProjection(new Projection().withProjectionType(ProjectionType.ALL)); // @Del
 
        // STUDENT TODO: Create attribute definitions for PatientId, City, Date.
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>(); // @Del
 
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("patientId").withAttributeType("S")); // @Del
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("city").withAttributeType("S")); // @Del
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("date").withAttributeType("S")); // @Del
 
        // STUDENT TODO: Create an instance of CreateTableRequest class.
        CreateTableRequest request = new CreateTableRequest() // @Del
                .withTableName(INFECTIONS_TABLE_NAME) // @Del
                .withKeySchema(new KeySchemaElement().withAttributeName("patientId").withKeyType(KeyType.HASH)) // @Del
                .withAttributeDefinitions(attributeDefinitions) // @Del
                .withProvisionedThroughput(new ProvisionedThroughput() // @Del
                        .withReadCapacityUnits(5L) // @Del
                        .withWriteCapacityUnits(10L)) // @Del
                .withGlobalSecondaryIndexes(gsi); // @Del
        
        Table table = dynamoDB.createTable(request); // @Del
 
        try { 
            System.out.println("Waiting for table to become active...");
            table.waitForActive(); 
        } catch (InterruptedException ie) { 
            ie.printStackTrace(); 
        } 
    }	
	
}
