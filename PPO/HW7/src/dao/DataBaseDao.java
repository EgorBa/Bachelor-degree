package dao;

import java.util.HashMap;

public class DataBaseDao<K, V> {
    private final HashMap<K, V> map;

    public DataBaseDao() {
        map = new HashMap<>();
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public void remove(K key) {
        map.remove(key);
    }

}
