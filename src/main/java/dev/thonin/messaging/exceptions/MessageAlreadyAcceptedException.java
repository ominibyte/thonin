package dev.thonin.messaging.exceptions;

public class MessageAlreadyAcceptedException extends RuntimeException {
    public MessageAlreadyAcceptedException(String message){
        super(message);
    }
}
