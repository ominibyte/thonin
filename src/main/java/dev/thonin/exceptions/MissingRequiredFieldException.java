package dev.thonin.exceptions;

public class MissingRequiredFieldException extends RuntimeException {
    public MissingRequiredFieldException(String message){
        super(message);
    }
}
