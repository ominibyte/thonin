package dev.thonin.messaging;

import java.util.Objects;

/**
 * This class models all messaging clients for all the supported protocols
 */
public abstract class Client {
    protected MessageHandler handler;

    public Client(MessageHandler handler){
        this.handler = Objects.requireNonNull(handler);
    }

    /**
     * Publish a message to the specified topic path
     * This is primarily for MQTT
     * @param topic The request type for a CoAP message or topic for an MQTT message
     * @param payload the message to send
     */
    public void publish(String topic, byte[] payload){}


    /**
     * Make GET request to the CoAP server.
     * This is primarily for CoAP
     */
    public void makeRequest(){
        makeRequest("GET");
    }

    /**
     * Make a request to the server using the specified mime format.
     * This is primarily for CoAP
     * @param requestType The type of request. E.g GET, DELETE, e.t.c
     */
    public void makeRequest(String requestType){
        makeRequest(requestType, null);
    }

    /**
     * Make a request to the server and send the payload across using text/plain mime format.
     * This is primarily for CoAP
     * @param requestType The type of request. E.g GET, POST, PUT, e.t.c
     * @param payload the request body
     */
    public void makeRequest(String requestType, byte[] payload){
        makeRequest(requestType, payload, "text/plain");
    }

    /**
     * Make a request to the server and send the payload across using the specified format.
     * This is primarily for CoAP
     * @param requestType The type of request. E.g GET, POST, PUT, e.t.c
     * @param payload the request body
     * @param mime the mime type. E.g application/json
     */
    public void makeRequest(String requestType, byte[] payload, String mime){}
}
