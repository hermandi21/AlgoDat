package Dictionary;

import java.util.EmptyStackException;
import java.util.Hashtable;
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
        //Fall2 - exisitiert nicht muss noch eingefuegt werden
        bucketEntries.add(new Entry<K,V>(key, value));
        size++;
        if(size > LOAD_FACTOR * hashTable.length){
            resizeTable();
        }
        return null;
        
    }

    @Override
    public V remove(K key) {
        
        //Fall: entry existiert bereits
        int hash = getHash(key);
        List<Entry<K,V>> bucketEntries = hashTable[hash];       //mit dem berechneten Hash-Wert gespeichert ist
        Iterator<Entry<K,V>> iterator = bucketEntries.iterator();


        while(iterator.hasNext()){
            Entry<K,V> entry = iterator.next();

            if(entry.getKey().equals(key)){
                V value = entry.getValue();
                iterator.remove();
                size--;
                return value;
            }
        }

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

 
    //Methode um auf primzahleigenschaft zu prüfen
    private static boolean isPrime(int zahl){
        if(zahl <= 1){
            return false;
        }
        for(int i = 2; i < Math.sqrt(zahl); i++){
            if(zahl % i == 0){
                return false;
            }
        }
        return true;
    }

    //Methode um mir die naechste Primzahl auszugeben
    private static int nextPrime(int zahl){
        if(zahl <= 1){
            zahl = 2;   // Da 2 die kleinste Primzahl ist
        }
        else{
            zahl++;
        }

        while(true){
            if(isPrime(zahl)){
                return zahl;    //Wenn die aktuelle Prime gefunden wurde, zurückgeben
            }
            zahl++; //Hier um eins erhoehen, einfach um zur naechsten Zahl zu wechseln
        }
    }

    private void resizeTable() {
        int newCapacity = nextPrime(hashTable.length * 2);
        List<Entry<K, V>>[] newTable = new LinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newTable[i] = new LinkedList<>();
        }
        for (List<Entry<K, V>> bucket : hashTable) {
            for (Entry<K, V> entry : bucket) {
                int hash = getHash(entry.getKey());
                List<Entry<K, V>> newBucket = newTable[hash];
                newBucket.add(entry);
            }
        }
        hashTable = newTable;
    }
    
  



}
