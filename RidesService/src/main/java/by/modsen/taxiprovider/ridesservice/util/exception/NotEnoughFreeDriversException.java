package by.modsen.taxiprovider.ridesservice.util.exception;


public class NotEnoughFreeDriversException extends Exception {

    public NotEnoughFreeDriversException() {
    }

    public NotEnoughFreeDriversException(String message) {
        super(message);
    }
}
