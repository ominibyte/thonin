package dev.thonin.messaging.exceptions;

public class MessageAlreadyRejectedException extends RuntimeException{
    public MessageAlreadyRejectedException(String message){
        super(message);
    }
}
