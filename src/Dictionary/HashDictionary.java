package Dictionary;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import javax.lang.model.element.Element;

public class HashDictionary<K extends Comparable<K>,V> implements Dictionary<K,V>{

    private LinkedList<Entry<K, V>>[] hashTable;      //hashTable als linear verkettete Liste
    private int size;
    private static final double LOAD_FACTOR = 2.0;
    private static final int DEF_CAPACITY = 31;            // Wir nehmen 31 weil es eine Primzahl ist


    // Konstruktor
    // Dieser erstellt eine linear verkettete Liste der Größe 16, was keine Primzahl ist
    @SuppressWarnings("unchecked")
    public HashDictionary(){
        size = 0;
        hashTable = new LinkedList[DEF_CAPACITY];

        for(int i = 0; i < DEF_CAPACITY; i++){
            hashTable[i] = new LinkedList<>();
        }
    }
  
    private int getHash(K key){
        return Math.abs(key.hashCode()) % hashTable.length;       //hashTablen Länge sollte eine Primzahl sein
    }

    @Override
    public V search(K key) {
        int hash = getHash(key);

        //Die DEF_CAP sagt darueber aus wie viele Buckets, also wie groß unsere hashTable ist
        List<Entry<K,V>> bucketEntries = hashTable[hash];

        for( Entry<K,V> elem : bucketEntries){
            if(elem.getKey().equals(key)){
                return elem.getValue();
            }
        }
        return null;
    }

    @Override
    public V insert(K key, V value) {
        
        //Fall1- existiert bereits
        int hash = getHash(key);

        List<Entry<K,V>> bucketEntries = hashTable[hash];
        for( Entry<K,V> elem : bucketEntries){
            if(elem.getKey().equals(key)){      //Wenn der Key gleich ist, also das elem schon exisitiert. Muss den value an den Key platzieren
                V oldValue = elem.getValue();
                elem.setValue(value);
                return oldValue;
            }
        }
        
        bucketEntries.add(new Entry<K,V>(key, value));
        size++;
        if(size > LOAD_FACTOR * hashTable.length){

        }

        //Fall2 - exisitiert nicht muss noch an die richtige Position eingefuegt werden
    }

    @Override
    public V remove(K key) {
        //Fall1- existiert bereits

        //Fall2 - exisitiert nicht muss noch an die richtige Position eingefuegt werden
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
