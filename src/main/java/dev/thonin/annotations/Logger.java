package dev.thonin.annotations;

import java.lang.annotation.*;

/**
 * This annotation captures the logger implementation.
 * This will be applied to classes and this will make an accessible field with the id and Logger as suffix
 * There will only be one instance of a Logger so it will be created in a factory class. If it has been created, we will
 * simply return the same instance. The same logger can be used in different classes
 */
@Repeatable(Loggers.class)
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Logger {
    String id();    //This is the name of the Logger that will be unique across the application
    Class<?> type();   //This is the type of data that will be stored in the logger
}
