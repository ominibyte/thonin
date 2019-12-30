package dev.thonin.common;

/**
 * This class models the lifecycle of an application
 */
public abstract class ThoninApp {
    /**
     * This method is called only once when the application is initializing. Resources can be provisioned here.
     */
    public void init(){}

    /**
     * This method is called after init just when the application is starting
     */
    public abstract void start();

    /**
     * This method is called before an application data is transferred due to mobility
     */
    public void pause(){}

    /**
     * This method is called to resume an application that has been transferred to another device due to mobility
     */
    public void resume(){}

    /**
     * This method is called before an application is closed to allow freeing up resources
     */
    public void destroy(){}
}
