package bia.apiassyncactivemq.exception;

public class ResourceNotFoundInExternalApiException extends RuntimeException {
    public ResourceNotFoundInExternalApiException(String message) {
        super(message);
    }
}