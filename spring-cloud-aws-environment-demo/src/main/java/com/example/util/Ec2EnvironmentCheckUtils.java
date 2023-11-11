package com.example.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.imds.Ec2MetadataClient;
import software.amazon.awssdk.imds.Ec2MetadataResponse;

/**
 * @author Ken Krueger
 */
public final class Ec2EnvironmentCheckUtils {

    private static Logger logger = LoggerFactory.getLogger(Ec2EnvironmentCheckUtils.class);
    
    private static Boolean isCloudEnvironment;
    private static Ec2MetadataClient client = Ec2MetadataClient.create();

    private Ec2EnvironmentCheckUtils() {
    }

    public static boolean isRunningOnCloudEnvironment() {
        if (isCloudEnvironment == null) {
            isCloudEnvironment = false;
            try {
                Ec2MetadataResponse response = client.get("/latest/meta-data/ami-id");
                isCloudEnvironment =  response.asString() != null && response.asString().length() > 0 ;
            }
            catch (SdkClientException e) {
                if (e.getMessage().contains("retries")) {
                    // Ignore any exceptions about exceeding retries.  
                    // This is expected when instance metadata is not available.
                }
            }
            catch (Exception e) {
                logger.error("Error occurred when accessing instance metadata.", e);
            } finally {
                if (isCloudEnvironment) {
                    logger.info("EC2 Instance MetaData detected, application is running within an EC2 instance.");
                } else { 
                    logger.info("EC2 Instance MetaData not detected, application is NOT running on an EC2 instance.");
                }
            }
        }
        return isCloudEnvironment;
    }

}
