package dev.thonin.components;

import dev.thonin.common.Pair;
import dev.thonin.events.SeriesEvent;
import dev.thonin.events.SeriesObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Series<E> implements Iterable<Pair<E, Long>>{
    private LinkedList<Pair<E, Long>> timeSeries;
    private List<SeriesObserver> observers;
    private final Object LOCK = new Object();

    public Series(){
        timeSeries = new LinkedList<>();
        observers = new ArrayList<>();
    }

    /**
     * Get all data between the given time
     * @param time1 The starting time inclusive
     * @param time2 The ending time inclusive
     * @return a list of all the available data within the time frame
     */
    public List<Pair<E, Long>> findDataBetween(Long time1, Long time2){
        return timeSeries.stream().filter(pair -> pair.getValue() >= time1 && pair.getValue() <= time2).collect(Collectors.toList());
    }

    /**
     * Get all the data available before the specified time.
     * This will return all the data that was logged having time less than the specified time
     * @param time the reference time
     * @return a list of data pairs
     */
    public List<Pair<E, Long>> findDataBefore(Long time){
        return timeSeries.stream().filter(pair -> pair.getValue() < time).collect(Collectors.toList());
    }

    /**
     * Get all the data available after the specified time.
     * This will return all the data that was logged having time greater than the specified time
     * @param time the reference time
     * @return a list of data pairs
     */
    public List<Pair<E, Long>> findDataAfter(Long time){
        return timeSeries.stream().filter(pair -> pair.getValue() > time).collect(Collectors.toList());
    }

    public Pair<E, Long> getLastPair(){
        return timeSeries.getLast();
    }

    public E getLastData(){
        return getLastPair().getKey();
    }

    public long getLastTime(){
        return getLastPair().getValue();
    }

    public E getFirstData(){
        return timeSeries.getFirst().getKey();
    }

    public void add(E data){
        add(data, System.currentTimeMillis());
    }

    public void add(E data, long time){
        add(new Pair<>(data, time));
    }

    public void add(Pair<E, Long> entry){
        timeSeries.add(entry);
    }

    public int size(){
        return timeSeries.size();
    }

    @Override
    public Iterator<Pair<E, Long>> iterator() {
        return timeSeries.iterator();
    }

    public void addObserver(SeriesObserver observer){
        synchronized (LOCK) {
            if (observer != null)
                observers.add(observer);
        }
    }

    public void removeObserver(SeriesObserver observer){
        synchronized (LOCK) {
            if (observer != null)
                observers.remove(observer);
        }
    }

    private void notifyEntryAdded(Pair<E, Long> data){
        synchronized (LOCK){
            SeriesEvent event = new SeriesEvent(System.currentTimeMillis(), data, this);
            observers.forEach(observer -> observer.entryAdded(event));
        }
    }
}
