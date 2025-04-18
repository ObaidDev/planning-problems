package com.trackswiftly.vehicle_routing.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TTLConcurrentMap<K, V> {
    

    private final ConcurrentMap<K, EntryWithTimestamp<V>> map = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final long ttlMillis;



    public TTLConcurrentMap(long ttlMillis) {
        this.ttlMillis = ttlMillis;
        // Schedule a cleanup task to run periodically
        scheduler.scheduleAtFixedRate(this::cleanup, ttlMillis, ttlMillis, TimeUnit.MILLISECONDS);
    }


    private static class EntryWithTimestamp<V> {
        final V value;
        final long timestamp;

        EntryWithTimestamp(V value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
    }




    public void put(K key, V value) {
        map.put(key, new EntryWithTimestamp<>(value, System.currentTimeMillis()));
    }



    // Get an entry (updates the timestamp if accessed)
    public V get(K key) {
        EntryWithTimestamp<V> entry = map.get(key);
        if (entry != null) {
            // Update the timestamp on access
            map.put(key, new EntryWithTimestamp<>(entry.value, System.currentTimeMillis()));
            return entry.value;
        }
        return null;
    }



    private void cleanup() {
        long now = System.currentTimeMillis();
        map.entrySet().removeIf(entry -> now - entry.getValue().timestamp > ttlMillis);
    }



    // Shutdown the scheduler when done
    public void shutdown() {
        scheduler.shutdown();
    }   
}
