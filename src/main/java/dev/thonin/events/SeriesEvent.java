package dev.thonin.events;

import dev.thonin.common.Pair;
import dev.thonin.components.Series;

public class SeriesEvent {
    private final long eventTime;
    private final Pair data;
    private Series series;      //The series that produced this event

    public SeriesEvent(long eventTime, Pair data, Series series){
        this.eventTime = eventTime;
        this.data = data;
        this.series = series;
    }

    public long getEventTime() {
        return eventTime;
    }

    public Pair getData() {
        return data;
    }

    public Series getSeries() {
        return series;
    }
}
