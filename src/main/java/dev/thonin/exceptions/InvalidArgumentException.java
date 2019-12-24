package dev.thonin.exceptions;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message){
        super(message);
    }
}
