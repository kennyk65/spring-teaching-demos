package com.example;

// Copyright 2015 Amazon Web Services, Inc. or its affiliates. All rights reserved.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

// The DynamoDBManager class sets up a DynamoDB table and loads it with pharmaceutical data. 
@Component
public class DynamoDBManager {

    public static final String PHARMA_TABLE_NAME = "PharmaceuticalsUsage";
   
    @Value("${lab.s3.bucket.name}") String S3_BUCKET_NAME;
    @Value("${lab.s3.bucket.region}") String S3_BUCKET_REGION;
    @Value("${pharma.data.file.key}") String PHARMA_DATA_FILE_KEY;
    

	@Autowired	AmazonS3 s3;	// Automatically setup by Spring Cloud AWS.
    @Autowired  DynamoDB dynamoDB;
    @Autowired  AmazonDynamoDBClient dynamoDBClient;


    @PostConstruct
    public void init() throws Exception {

        System.out.println("Beginning DynamoDB setup.");
        if (!pharmaDataExists()) {
            createPharmaTable();
        }
        loadData();
        System.out.println("Completed DynamoDB setup.");
    }

    private  boolean pharmaDataExists() {
        boolean pharmaDataExists = false;
        try {
            dynamoDBClient.describeTable(PHARMA_TABLE_NAME);
            pharmaDataExists = true;
        } catch (ResourceNotFoundException e) {
            System.out.println(PHARMA_TABLE_NAME + " DynamoDB table does not exist.");
        }
        return pharmaDataExists;
    }

    private  void createPharmaTable() {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();

        attributeDefinitions.add(new AttributeDefinition()
        	.withAttributeName("DrugName")
        	.withAttributeType("S"));

        CreateTableRequest request = new CreateTableRequest()
        	.withTableName(PHARMA_TABLE_NAME)
            .withKeySchema(new KeySchemaElement()
            		.withAttributeName("DrugName")
            		.withKeyType(KeyType.HASH))
            .withAttributeDefinitions(attributeDefinitions)
            .withProvisionedThroughput(new ProvisionedThroughput()
            		.withReadCapacityUnits(5L)
            		.withWriteCapacityUnits(5L));

        System.out.printf("Creating %s table. %n", PHARMA_TABLE_NAME);

        Table table = dynamoDB.createTable(request);

        try {
            table.waitForActive();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    
    /**
     * Loads pharmaceutical usage data from CSV file into DynamoDB table. 
     */
    private  void loadData() {
        S3Object pharmaDataObject = null;
        BufferedReader br = null;
        String line = "";
        String splitter = ",";

        try {
            pharmaDataObject = s3.getObject(S3_BUCKET_NAME, PHARMA_DATA_FILE_KEY);
            if (pharmaDataObject != null) {
                Table table = dynamoDB.getTable(PHARMA_TABLE_NAME);
                br = new BufferedReader(new InputStreamReader(pharmaDataObject.getObjectContent()));

                while ((line = br.readLine()) != null) {
                    String[] pharmaDataAttrValues = line.split(splitter);
                    try {
                        if (!pharmaDataAttrValues[0].equals("DrugName")) {
                            // CSV attributes: DrugName, Usage
                            Item item = new Item()
                            	.withPrimaryKey("DrugName", pharmaDataAttrValues[0])
                                .withString("Usage", pharmaDataAttrValues[1]);
                            table.putItem(item);
                            System.out.println("Added item to table:" + line);
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to create item in table" + line);
                        System.err.println(e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("DynamoDB data upload complete.");
    }

    public String getPharmaInfo(String drugName) {
        String info = "Unknown item";
    	Item item = dynamoDB.getTable(PHARMA_TABLE_NAME).getItem("DrugName", drugName);
        if (item != null) {
        	info = item.get("Usage").toString();
        }
        return info;
    }
    
}
