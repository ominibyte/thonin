package dev.thonin.components;

import dev.thonin.common.Pair;
import dev.thonin.events.LoggerEvent;
import dev.thonin.events.LoggerObserver;
import dev.thonin.events.SeriesEvent;
import dev.thonin.events.SeriesObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Logger<E> implements SeriesObserver {
    private ConcurrentMap<String, Series<E>> seriesMap;
    private List<LoggerObserver> observers;

    private final Object LOCK = new Object();

    public Logger(){
        seriesMap = new ConcurrentHashMap<>();
        observers = new ArrayList<>();
    }

    public Series<E> getSeries(String deviceId){
        return seriesMap.get(deviceId);
    }

    public void addEntry(String id, E data){
        addEntry(id, data, System.currentTimeMillis());
    }

    public void addEntry(String id, E data, long time){
        addEntry(id, new Pair<>(data, time));
    }

    public void addEntry(String id, Pair<E, Long> entry){
        Series<E> series = seriesMap.get(id);
        if( series == null ) {
            series = new Series<>();
            series.addObserver(this);
        }
        series.add(entry);

        seriesMap.putIfAbsent(id, series);
    }

    public int size(){
        return seriesMap.size();
    }

    public void addObserver(LoggerObserver observer){
        synchronized (LOCK) {
            if( observer != null )
                observers.add(observer);
        }
    }

    public void removeObserver(LoggerObserver observer){
        synchronized (LOCK){
            if( observer != null )
                observers.remove(observer);
        }
    }


    @Override
    public void entryAdded(SeriesEvent event) {
        seriesMap.entrySet().stream().filter(entry -> entry.getValue() == event.getSeries())
                .findFirst().ifPresent(entry -> notifyAddedEntry(entry.getKey(), event));
    }

    private void notifyAddedEntry(String deviceId, SeriesEvent seriesEvent){
        synchronized (LOCK) {
            LoggerEvent event = new LoggerEvent(deviceId, seriesEvent.getEventTime(), seriesEvent.getData(),
                    seriesEvent.getSeries(), this);
            observers.forEach(observer -> observer.entryAdded(event));
        }
    }
}
