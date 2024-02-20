package by.modsen.taxiprovider.passengerservice.util.exception;

import java.util.function.Supplier;

public class EntityNotFoundException extends Exception{
    public EntityNotFoundException(String message) {
        super(message);
    }

    public static Supplier<EntityNotFoundException> entityNotFoundException(String message) {
        return () -> new EntityNotFoundException(message);
    }
}
