package dev.thonin.events;

import dev.thonin.common.Pair;
import dev.thonin.components.Logger;
import dev.thonin.components.Series;

public class LoggerEvent {
    private final String deviceId;    //The ID of the device that generated this event
    private final long eventTime;     //The time this event was generated
    private final Pair data;          //The associated data
    private final Series series;      //The series that generated this event
    private final Logger logger;      //The logger that triggered this event

    public LoggerEvent(String deviceId, long eventTime, Pair data, Series series, Logger logger){
        this.deviceId = deviceId;
        this.eventTime = eventTime;
        this.data = data;
        this.series = series;
        this.logger = logger;
    }

    public Pair getData() {
        return data;
    }

    public long getEventTime() {
        return eventTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Series getSeries() {
        return series;
    }

    public Logger getLogger() {
        return logger;
    }
}
