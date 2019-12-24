package dev.thonin.runtime;

import dev.thonin.components.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps track of all defined components and provides an easy access to retrieve them for applications
 */
public class ComponentContainer {
    public static final ComponentContainer instance = new ComponentContainer();

    private Map<String, Logger> loggers;    //The key is the variable name of the logger
    //TODO add definitions of other types of components here like broadcasters,

    private ComponentContainer(){
        loggers = new HashMap<>();
    }

    public Logger getLogger(String id){
        return loggers.get(id);
    }

    public void createLogger(String id){
        loggers.putIfAbsent(id, new Logger());
    }
}
