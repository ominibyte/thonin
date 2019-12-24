package dev.thonin.messaging;

import dev.thonin.messaging.exceptions.MessageAlreadyAcceptedException;
import dev.thonin.messaging.exceptions.MessageAlreadyRejectedException;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class houses a message and the protocol object as well as the ability to respond to a received message
 */
public class MessageContext {
    private final MessageType messageType;
    private final Function<Boolean, Message> acceptor;
    private final Consumer<byte[]> replier;
    private Message message;
    private byte accepted = 0;
    private byte rejected = 0;

    public MessageContext(MessageType messageType, Function<Boolean, Message> acceptor, Consumer<byte[]> replier){
        this.messageType = messageType;
        this.acceptor = acceptor;
        this.replier = replier;
    }

    public Message accept(){
        if( message != null )
            return message;

        if( rejected == 1 )
            throw new MessageAlreadyRejectedException("This message has already been rejected!");

        accepted = 1;

        message = acceptor.apply(true);
        return message;
    }

    public void reject(){
        if( rejected == 1 )
            return;

        if( accepted == 1 )
            throw new MessageAlreadyAcceptedException("This message has already been accepted!");

        rejected = 1;
        acceptor.apply(false);
    }

    public MessageType getMessageType(){
        return messageType;
    }

    public void respond(String message){
        respond(message.getBytes());
    }

    public void respond(byte[] message){
        if( rejected == 1 )
            throw new MessageAlreadyRejectedException("This message has already been rejected. You can no longer respond.");

        //TODO maybe we may want to make sure only one response can be sent
        replier.accept(message);
    }
}
