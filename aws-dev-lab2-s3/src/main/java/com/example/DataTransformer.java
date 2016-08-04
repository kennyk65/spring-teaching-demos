package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Component
public class DataTransformer {

	Logger logger = LoggerFactory.getLogger(DataTransformer.class);
	
	//	The AmazonS3 client is already setup for us by Spring Cloud AWS,
	//	All we have to do is inject and use:
	@Autowired AmazonS3 s3;


    public static final String[] ATTRS = { "genericDrugName", "adverseReaction" };

    // STUDENT TODO: Update input bucket name to a unique bucket name.
    public static final String INPUT_BUCKET_NAME = "input-someuniquename-123-kk";

    // STUDENT TODO: Update output bucket name to a unique bucket name.
    public static final String OUTPUT_BUCKET_NAME = "output-someuniquename-123-kk";

    public static final String JSON_COMMENT = "\"comment\": \"DataTransformer JSON\",";


    // List used to store pre-signed URLs generated.
    public static List<URL> presignedUrls = new ArrayList<>();

    
	//	Run as soon as Bean is instantiated:
	@PostConstruct
	public void atStartup() {
        ObjectListing inputFileObjects = null;
        String fileKey = null;
        S3Object s3Object = null;
        File transformedFile = null;
        int fileCount = 0;

        try {
            logger.info("Transformer: Here we go...");
            createInputBucketIfNeeded();
            createOutputBucketIfNeeded();

            inputFileObjects = s3.listObjects(INPUT_BUCKET_NAME);

            do {
                for (S3ObjectSummary objectSummary : inputFileObjects.getObjectSummaries()) {
                    fileKey = objectSummary.getKey();
                    logger.debug("Transformer: Transforming file: " + fileKey);
                    if (fileKey.endsWith(".txt")) {
                        // STUDENT TODO: Retrieve each object from the input S3 bucket.
                        s3Object = s3.getObject(INPUT_BUCKET_NAME, fileKey); // @Del
                        // Transform object's content from CSV to JSON format.
                        transformedFile = transformText(s3Object); 
                        putObjectBasic(OUTPUT_BUCKET_NAME, fileKey, transformedFile);                        
                        // putObjectEnhanced(OUTPUT_BUCKET_NAME, fileKey, transformedFile);
                        generatePresignedUrl(OUTPUT_BUCKET_NAME, fileKey);
                        fileCount++;
                    }
                }
                inputFileObjects = s3.listNextBatchOfObjects(inputFileObjects);
            } while (inputFileObjects.isTruncated());

            logger.info("Transformer: DONE, " + fileCount + " files transformed.");
            printPresignedUrls();
        } catch (AmazonServiceException ase) {
            logger.error("\nError Message:    " + ase.getMessage() + 
            			"\nHTTP Status Code: " + ase.getStatusCode() +
            			"\nAWS Error Code:   " + ase.getErrorCode() +
            			"\nError Type:       " + ase.getErrorType() +
            			"\nRequest ID:       " + ase.getRequestId() );
        } catch (AmazonClientException ace) {
            logger.error("Error Message: " + ace.getMessage());
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    // Reads the input stream of the S3 object. Transforms content to JSON format.
    // Return the transformed text in a File object.
    private File transformText(S3Object s3Object) throws IOException {
        File transformedFile = new File("transformedfile.txt");
        String inputLine = null;
        StringBuffer outputStrBuf = new StringBuffer(1024);
        outputStrBuf.append("[\n");

        try (java.io.InputStream is = s3Object.getObjectContent();
                java.util.Scanner s = new java.util.Scanner(is);
                FileOutputStream fos = new FileOutputStream(transformedFile)) {
            s.useDelimiter("\n");
            while (s.hasNextLine()) {
                inputLine = s.nextLine();
                outputStrBuf.append(transformLineToJson(inputLine));
            }
            // Remove trailing comma at the end of the content. Close the array.
            outputStrBuf.deleteCharAt(outputStrBuf.length() - 2);
            outputStrBuf.append("]\n");
            fos.write(outputStrBuf.toString().getBytes());
            fos.flush();

        } catch (IOException e) {
            logger.error("Transformer: Unable to create transformed file");
            e.printStackTrace();
        }

        return transformedFile;
    }

    private String transformLineToJson(String inputLine) {
        String[] inputLineParts = inputLine.split(",");
        int len = inputLineParts.length;

        String jsonAttrText = "{\n  " + JSON_COMMENT + "\n";
        for (int i = 0; i < len; i++) {
            jsonAttrText = jsonAttrText + "  \"" + ATTRS[i] + "\"" + ":" + "\"" + inputLineParts[i] + "\"";
            if (i != len - 1) {
                jsonAttrText = jsonAttrText + ",\n";
            } else {
                jsonAttrText = jsonAttrText + "\n";
            }
        }
        jsonAttrText = jsonAttrText + "},\n";
        return jsonAttrText;
    }

    private void putObjectBasic(String bucketName, String fileKey, File transformedFile) {
        // STUDENT TODO: Upload object to output bucket.        
        s3.putObject(OUTPUT_BUCKET_NAME, fileKey, transformedFile); // @Del
    }
    
    private void putObjectEnhanced(String bucketName, String fileKey, File transformedFile) {

        PutObjectRequest putRequest = new PutObjectRequest(bucketName, fileKey, transformedFile);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // STUDENT TODO: Enable server side encryption.
        objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION); // @Del
        
        // STUDENT TODO: Add user metadata. "contact", "John Doe"
        objectMetadata.addUserMetadata("contact", "John Doe"); // @Del
        
        // STUDENT TODO: Upload object to output bucket.
        putRequest.setMetadata(objectMetadata); // @Del

        PutObjectResult response = s3.putObject(putRequest); // @Del

        logger.info("Transformer: Encryption status of uploaded object: " + response.getSSEAlgorithm()); // @Del
        logger.info("Transformer: User metadata for name = 'contact': " + objectMetadata.getUserMetaDataOf("contact"));
    }

    private void generatePresignedUrl(String bucketName, String objectKey) {
        URL url = null;

        java.util.Date expiration = new java.util.Date();
        long msec = expiration.getTime();
        msec += 1000 * 60 * 15; // 15 minutes
        expiration.setTime(msec);

        // STUDENT TODO: Generate a pre-signed URL to retrieve object (GET).
        GeneratePresignedUrlRequest generatePresignedUrlRequest = // @Del
        new GeneratePresignedUrlRequest(bucketName, objectKey); // @Del
        generatePresignedUrlRequest.setMethod(HttpMethod.GET); // @Del
        generatePresignedUrlRequest.setExpiration(expiration); // @Del

        url = s3.generatePresignedUrl(generatePresignedUrlRequest); // @Del

        if (url != null) {
            presignedUrls.add(url);
        }
    }

    private void printPresignedUrls() {
        logger.info("Transformer: Pre-signed URLs: ");
        for (URL url : presignedUrls) {
            logger.info(url + "\n");
        }
    }
    
    // Create the input bucket if it does not exist already.
    private void createInputBucketIfNeeded() throws Exception {
    	createBucketIfNeeded(INPUT_BUCKET_NAME, "INput" );
    	populateTestFiles();
    }    

    // Create the output bucket if it does not exist already.
    private  void createOutputBucketIfNeeded() throws Exception {
    	createBucketIfNeeded(OUTPUT_BUCKET_NAME, "output" );
    }    

    // Create the given bucket if it does not already exist.  Verify ownership.
    private  void createBucketIfNeeded(String bucketName, String bucketDescription) throws Exception {
        if (!s3.doesBucketExist(bucketName)) {
            logger.info("Transformer: Creating " + bucketDescription + " bucket: " + bucketName);
            s3.createBucket(bucketName);
        } else {
            verifyBucketOwnership(bucketName);
        }
    }    

    //	Populate input with test files:
    private void populateTestFiles() {
        logger.info("Transformer: Input files missing, copying some test files to " + INPUT_BUCKET_NAME);
        s3.copyObject(
        		"us-west-2-aws-training",
            	"awsu-ilt/developing/v2.0/lab-2-s3/static/SampleInputFiles/DrugAdverseEvents_September.txt", 
            	INPUT_BUCKET_NAME, 
            	"DrugAdverseEvents_September.txt");
        s3.copyObject(
        		"us-west-2-aws-training",
            	"awsu-ilt/developing/v2.0/lab-2-s3/static/SampleInputFiles/DrugAdverseEvents_October.txt", 
            	INPUT_BUCKET_NAME, 
            	"DrugAdverseEvents_October.txt");
    }    
    
    // Verify that this AWS account is the owner of the bucket.
    public void verifyBucketOwnership(String bucketName) throws Exception {
        boolean ownedByYou = false;
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets) {
            if (bucket.getName().equals(bucketName)) {
                ownedByYou = true;
                break;
            }
        }
        if (!ownedByYou) {
            String msg = String.format("The %s bucket is owned by another account. Specify a unique name for your bucket.", bucketName);
            throw new Exception(msg);
        }
    }

}
