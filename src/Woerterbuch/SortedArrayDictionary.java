package Woerterbuch;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
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
            } else if (key.compareTo(data[m].getKey()) < 0) {   //Linke Haelfte weitersuchen
                re = m - 1;
            } else {
                li = m + 1;                                  //rechte Haelfte weitersuchen
            }

        }
        return null;
    }

    private int searchKey(K key){
        for (int i = 0; i < size; i++){
            if (data[i].getKey().equals(key)){
                return i;
            }
        }
        return -1;      //falls key nicht gefunden wurde
    }

    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);

        if(i >= 0){     //Wenn i > 0 ist, d.h. es ist nicht negativ und somit hat der key einen Platz i data[]
            //Wir überschreiben die vorhandene Postition
            V r = data[i].getValue();
            data[i].setValue(value);        //hier benutze ich setValue-Methode um den Wert zu ueberschreiben
            return r;
        }

        //Falls es ein Neueintrag ist
        if (data.length == size){
            data = Arrays.copyOf(data, 2 * size);   //Verdoppeln des Arrayfeldes ist eine gaengige Methode bei vollgelaufenem Array
        }

        data[size] = new Entry<K, V>(key, value);
        size++;
        return null;
    }

    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if(i == -1)
            return null;

        //Datensatz loeschen und mit for Schleife wird jedes Elem
        V r = data[i].getValue();
        for (int j = i; j < size - i; j++){
            data[j] = data[j+1];

        data[--size] = null;
        return r;
        }
    }



    @Override
    public int size() {
        return size;
    }


    @Override
    public void forEach(Consumer<? super Entry<K, V>> action) {
        Dictionary.super.forEach(action);
    }

    @Override
    public Spliterator<Entry<K, V>> spliterator() {
        return Dictionary.super.spliterator();
    }


    @Override
    public Iterator<Entry<K, V>> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }
}
