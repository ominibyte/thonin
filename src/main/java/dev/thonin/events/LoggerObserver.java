package dev.thonin.events;

public interface LoggerObserver {
    void entryAdded(LoggerEvent event);
}
