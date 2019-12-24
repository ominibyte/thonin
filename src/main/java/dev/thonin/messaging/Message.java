package dev.thonin.messaging;

import java.util.Map;

public class Message {
    private final MessageType type;
    private final byte[] payload;
    private final MessageProtocol protocol;
    private final Map<String, Object> metadata;

    public Message(MessageType type, byte[] payload, MessageProtocol protocol, Map<String, Object> metadata) {
        this.type = type;
        this.payload = payload;
        this.protocol = protocol;
        this.metadata = metadata;
    }

    public byte[] getPayload() {
        return payload;
    }

    public MessageProtocol getProtocol() {
        return protocol;
    }

    public MessageType getType() {
        return type;
    }

    public Object get(String key){
        return metadata.get(key);
    }

    public Object has(String key){
        return get(key) != null;
    }
}
