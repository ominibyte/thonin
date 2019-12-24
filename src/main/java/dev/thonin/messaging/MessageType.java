package dev.thonin.messaging;

public enum MessageType {
    DEFAULT, GET, POST, PUT, DELETE, FETCH, PATCH, IPATCH;

    public static MessageType findType(String type){
        for(MessageType messageType : MessageType.values()){
            if( messageType.toString().equals(type) )
                return messageType;
        }
        return null;
    }
}
