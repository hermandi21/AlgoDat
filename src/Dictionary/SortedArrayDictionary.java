package Dictionary;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class SortedArrayDictionary<K extends Comparable<K>, V> implements Dictionary<K, V> {

    private static final int DEF_CAPACITY = 16;
    private int size;
    private Entry<K,V>[] data;


    @SuppressWarnings("unchecked")
    public SortedArrayDictionary(){
        size = 0;
        data = new Entry[DEF_CAPACITY];
    }


    @Override
    public V search(K key) {
        int li = 0;
        int re = size - 1;

        while (re >= li) {
            int m = (li + re) / 2;

            if (key.compareTo(data[m].getKey()) == 0) {
                return data[m].getValue();
            } else if (key.compareTo(data[m].getKey()) < 0) {
                re = m - 1;
            } else {
                li = m + 1;
            }
        }

        return null; // falls key nicht gefunden wurde
    }

    private int searchKey(K key){
        for (int i = 0; i < size; i++){
            if (data[i].getKey().equals(key)){
                return i;
            }
        }
        return -1;      //falls key nicht gefunden wurde
    }

    public V insert(K key, V value) {
        int i = searchKey(key);

        if (i >= 0) {
            // Eintrag bereits vorhanden, überschreiben
            V oldValue = data[i].getValue();
            data[i].setValue(value);
            return oldValue;
        } else {
            // Neuen Eintrag anlegen
            if (data.length == size) {
                data = Arrays.copyOf(data, 2 * size);
            }
            int j = size - 1;
            while (j >= 0 && key.compareTo(data[j].getKey()) < 0) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = new Entry<>(key, value);
            size++;
            return null;
        }
    }


    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if(i == -1)
            return null;

        //Datensatz loeschen und mit for Schleife wird jedes Elem
        V r = data[i].getValue();
        for (int j = i; j < size - 1; j++){
            data[j] = data[j+1];
        }

        data[--size] = null;
        return r;
    }



    @Override
    public int size() {
        return size;
    }


    @Override
    public void forEach(Consumer<? super Entry<K, V>> action) {
        Dictionary.super.forEach(action);
    }

    public Iterator<Entry<K, V>> iterator() {
        return new SortedArrayDictionaryIterator();
    }

    private class SortedArrayDictionaryIterator implements Iterator<Entry<K, V>> {

        private int currentIndex;

        public SortedArrayDictionaryIterator() {
            currentIndex = 0;
            while (currentIndex < size && data[currentIndex] == null){  //Solange currentIndex kleiner als die Größe des Entry<K,V>-Arrays ist und
                currentIndex++;                                         // und data[currentIndex] noch leer ist
            }
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Ende des Wörterbuchs erreicht");
            }
            Entry<K, V> entry = data[currentIndex]; //Aktuelles element wird als entry gespeichert
            currentIndex++;

            while (currentIndex < size && data[currentIndex] == null){
                currentIndex++;
            }

            return entry;
        }
    }


}

