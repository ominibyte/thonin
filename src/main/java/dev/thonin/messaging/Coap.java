package dev.thonin.messaging;

import dev.thonin.common.AppInfo;
import dev.thonin.exceptions.InvalidArgumentException;
import dev.thonin.exceptions.MissingRequiredFieldException;
import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class models creating and operating CoAP server and client. The inner Server class wraps the CoapServer in the
 * underlying library.
 * objects for more control over response to the client.
 */
public class Coap {
    private static final int COAP_DEFAULT_PORT = 5683;
    private static final int COAP_DEFAULT_TLS_PORT = 5684;

    /**
     * Create a Coap Client to make requests to a Coap Server using serverUrl and receive response using MessageHandler
     * @param handler the MessageHandler to receive messages from the Coap Server
     * @param serverUrl the full URL resource path on the server
     * @return a Client instance
     */
    public static Client createClient(MessageHandler handler, String serverUrl){
        return new CoapClient(handler, serverUrl);
    }

    /**
     * Create a builder which can be used to create a CoapClient instance
     * @return the builder instance for more operations
     */
    public static CoapClientBuilder create(){
        return new CoapClientBuilder(null);
    }

    /**
     * Create a builder starting with a MessageHandler which can be used to create a CoapClient instance
     * @return the builder instance for more operations
     */
    public static CoapClientBuilder create(MessageHandler handler){
        return new CoapClientBuilder(handler);
    }

    /**
     * Create a CoAP server running on the default port
     * @param handler listener to receive and respond to requests
     * @return CoAP server instance
     */
    public static Server createServer(MessageHandler handler){
        return new Server(handler, (int[]) null);
    }

    /**
     * Create a CoAP server running on the specified port
     * @param handler listener to receive and respond to requests
     * @return CoAP server instance
     */
    public static Server createServer(MessageHandler handler, int port){
        return new Server(handler, port);
    }

    /**
     * CoAP Server Wrapper
     * Messages received from Clients in this class have the following properties:
     *  coapExchange and exchange to expose the underlying objects from the raw request
     */
    private static class Server{
        private CoapServer coapServer;

        private Server(MessageHandler handler, int...ports){
            Objects.requireNonNull(handler);

            coapServer = new CoapServer(ports);
            coapServer.add(new CoapResource(AppInfo.getAppName()){
                @Override
                public void handleRequest(Exchange exchange) {
                    MessageType messageType = MessageType.findType(exchange.getResponse().getCode().toString());
                    CoapExchange coapExchange = new CoapExchange(exchange, this);

                    handler.onResponse(new MessageContext(messageType, (accepted) -> {
                        if( !accepted ){
                            if( messageType ==  MessageType.POST )
                                coapExchange.reject();
                            else
                                coapExchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED);

                            return null;
                        }

                        // here we send accept for CoAP POST
                        if( messageType ==  MessageType.POST )
                            coapExchange.accept();

                        // create message
                        Map<String, Object> map = new HashMap<>();
                        map.put("coapExchange", coapExchange);
                        map.put("exchange", exchange);

                        return new Message(messageType, coapExchange.getRequestPayload(), MessageProtocol.COAP, map);
                    }, response -> coapExchange.respond(new String(response)) ));
                }
            });
        }

        /**
         * Starts the CoAP server
         */
        public void start(){
            coapServer.start();
        }
    }

    /**
     * CoAP Client Wrapper
     * Messages received from the Server in this class have the following properties:
     *  coapResponse -> to expose the underlying response object
     */
    private static class CoapClient extends Client{
        private org.eclipse.californium.core.CoapClient client;
        private CoapHandler coapHandler;

        public CoapClient(MessageHandler handler, String url){
            super(handler);
            Objects.requireNonNull(url);

            client = new org.eclipse.californium.core.CoapClient(url);
            coapHandler = new CoapHandler() {
                @Override
                public void onLoad(CoapResponse coapResponse) {
                    handler.onResponse(new MessageContext(MessageType.DEFAULT, (accepted) -> {
                        if(!accepted)
                            return null;
                        // create message
                        Map<String, Object> map = new HashMap<>();
                        map.put("coapResponse", coapResponse);

                        return new Message(MessageType.DEFAULT, coapResponse.getPayload(), MessageProtocol.COAP, map);
                    }, r -> {}));
                }

                @Override
                public void onError() {
                    handler.onFailure();
                }
            };
        }

        @Override
        public void makeRequest(String requestType, byte[] payload, String mime) {
            int messageType = MediaTypeRegistry.parse(mime);

            switch ( requestType.toUpperCase() ){
                case "GET": client.get(coapHandler); break;
                case "POST": client.post(coapHandler, payload, messageType); break;
                case "PUT": client.put(coapHandler, payload, messageType); break;
                case "DELETE": client.delete(coapHandler); break;
                default: throw new InvalidArgumentException("Request type is not supported. " +
                        "For other request types please use the raw client object.");
            }
        }

        /**
         * Get the raw Client object for more advanced operations
         * @return an instance of the raw client from org.eclipse.californium.core.CoapClient.
         */
        public org.eclipse.californium.core.CoapClient getRawClient(){
            return client;
        }
    }

    private static class CoapClientBuilder{
        private String server;
        private int port = -1;
        private String path;
        private boolean useTls;
        private boolean useTcp;
        private boolean useWebsocket;
        private Map<String, String> queries;
        private MessageHandler handler;

        public CoapClientBuilder(MessageHandler handler){
            this.handler = handler;
            queries = new HashMap<>();
        }

        /**
         * Set the server name. E.g example.com
         * @param server the server name
         * @return the builder instance
         */
        public CoapClientBuilder server(String server){
            this.server = server;
            return this;
        }

        /**
         * Set the server port number
         * @param port the server port
         * @return the builder instance
         */
        public CoapClientBuilder port(int port){
            this.port = port;
            return this;
        }

        /**
         * Set the path on the server to access. E.g /some/path
         * @param path the path on the server with the resource
         * @return the builder instance
         */
        public CoapClientBuilder path(String path){
            this.path = path;
            return this;
        }

        /**
         * Set whether to use TLS or not
         * @param useTls if request should be made using
         * @return the builder instance
         */
        public CoapClientBuilder useTls(boolean useTls){
            this.useTls = useTls;
            return this;
        }

        /**
         * Set request to be made using TLS
         * @return the builder instance
         */
        public CoapClientBuilder useTls(){
            return useTls(true);
        }

        /**
         * Set the MessageHandler that will listen to response from the server
         * @param handler the MessageHandler object
         * @return the builder instance
         */
        public CoapClientBuilder handler(MessageHandler handler){
            this.handler = handler;
            return this;
        }

        /**
         * Specify if to use TCP or UDP. UDP is the default.
         * @param useTcp if true TCP will be used instead of UDP
         * @return the builder instance
         */
        public CoapClientBuilder useTcp(boolean useTcp){
            this.useTcp = useTcp;
            if( useTcp )
                useWebsocket = false;
            return this;
        }

        /**
         * Specify if to use TCP instead of UDP
         * @return the builder instance
         */
        public CoapClientBuilder useTcp(){
            return useTcp(true);
        }

        /**
         * Specify if to use Websocket
         * @param useWebsocket if true Websocket will be used
         * @return the builder instance
         */
        public CoapClientBuilder useWebsocket(boolean useWebsocket){
            this.useWebsocket = useWebsocket;
            if( useWebsocket )
                useTcp = false;
            return this;
        }

        /**
         * Specify if to use Websocket
         * @return the builder instance
         */
        public CoapClientBuilder useWebsocket(){
            return useWebsocket(true);
        }

        /**
         * Add a GET query to the request. E.g ?key=value
         * @param key The query key
         * @param value The query value
         * @return the builder instance
         */
        public CoapClientBuilder query(String key, String value){
            queries.put(key, value);
            return this;
        }


        /**
         * Build the client with the specified properties
         * @return a Client instance
         */
        public Client build(){
            if( server == null )
                throw new MissingRequiredFieldException("Coap Server URL cannot be left empty");
            if( handler == null )
                throw new MissingRequiredFieldException("Message Handler is required");

            int port = this.port == -1 ? (useTls ? COAP_DEFAULT_TLS_PORT : COAP_DEFAULT_PORT) : this.port;
            String url = "coap" + (useTls ? "s" : "");
            if( useTcp )
                url += "+tcp";
            if( useWebsocket )
                url += "+ws";
            url += "://";
            url += server + ":" + port;
            if( path != null )
                url += (!path.startsWith("/") ? "/" : "") + path;
            if( !queries.isEmpty() ){
                url += "?";

                StringBuilder builder = new StringBuilder();
                int count = 0;
                for (Map.Entry<String, String> entry : queries.entrySet()) {
                    if( count > 0 )
                        builder.append("&");

                    builder.append(entry.getKey()).append("=").append(entry.getValue());
                    count++;
                }

                url += builder.toString();
            }

            return new CoapClient(handler, url);
        }
    }
}
