package by.modsen.taxiprovider.endtoendtest.exception;

public class FeignClientException extends RuntimeException {

    public FeignClientException(String message) {
        super(message);
    }
}
