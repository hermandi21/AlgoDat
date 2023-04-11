package Dictionary;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

public class HashDictionary<K,V> implements Dictionary<K,V>{

    private LinkedList<Entry<K, V>>[] hashtabelle;      //Hashtabelle als linear verkettete Liste
    private int size;
    private static final double LOAD_FACTOR = 2.0;
    private static final int DEF_CAPACITY


    @Override
    public V insert(K key, V value) {
        return null;
    }

    @Override
    public V search(K key) {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Entry<K, V>> action) {
        Dictionary.super.forEach(action);
    }
}
