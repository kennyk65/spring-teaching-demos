package org.springframework.cloud.serverless;

public class ServerlessException extends RuntimeException {

	private static final long serialVersionUID = 3667732826940994901L;

    public ServerlessException(String message) {
        super(message);
    }
    
    public ServerlessException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
