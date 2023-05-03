package Dictionary;


import java.util.Iterator;

/**
 * Implementation of the Dictionary interface as AVL tree.
 * <p>
 * The entries are ordered using their natural ordering on the keys, 
 * or by a Comparator provided at set creation time, depending on which constructor is used. 
 * <p>
 * An iterator for this dictionary is implemented by using the parent node reference.
 * 
 * @param <K> Key.
 * @param <V> Value.
 */
public class BinaryTreeDictionary<K extends Comparable<? super K>,V> implements Dictionary<K,V> {

    static private class Node<K, V> {
        K key;
        V value;
        int height;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K k, V v) {
            key = k;
            value = v;
            height = 0;
            left = null;
            right = null;
            parent = null;
        }


    }

    //Anfangsdeklaration Wurzelknoten wird null gesetzt
    private Node<K,V> rootNode = null;
    public int size = 0;



    //Hilfsmethode für search-Methode im BST
    public V search(K key){
        return searchR(key, rootNode);
    }

    // rekursive Search-Methode(Folie 3-10)
    /**
     * @param key
     * @param parentNode
     * @return
     */
    private V searchR(K key, Node<K,V> parentNode){
        //Sicherheitsabfrage
        if(parentNode == null){
            return null;
        }
        else if (key.compareTo(parentNode.key) < 0) {   // key < parentNode --> links gehen
            return searchR(key, parentNode.left);
        }
        else if (key.compareTo(parentNode.key) > 0) {   // key > parentNode --> rechts gehen
            return searchR(key, parentNode.right);
        }
        else
            return parentNode.value;

    }


    private V oldValue;//Rueckgabeparameter

    //Aufrufmethode für insertR
    public V insert(K key, V value){
        rootNode = insertR(key, value, rootNode);
        //Parent-Zeiger einsetzen
        if(rootNode != null){
            rootNode.parent = null; //Ich möchte dass der Elternzeiger auf null gesetzt wird
        }
        return oldValue;
    }
    //rekursive InsertMethode
    private Node<K,V> insertR(K key, V value, Node<K,V> parentNode){
        //Prüfe ob Baum vorhanden ist
        if(parentNode == null){
            parentNode = new Node<K,V>(key, value);
            size++;

        }
        else if (key.compareTo(parentNode.key) < 0){
            //Elternzeiger einsetzen
            if(parentNode.left != null){
                parentNode.left.parent = parentNode;
            }
            //----------------------
            parentNode.left = insertR(key, value, parentNode.left);
        }
        else if(key.compareTo(parentNode.key) > 0){
            //Elternknoten einsetzen
            if(parentNode.right != null){
                parentNode.right.parent = parentNode;
            }
            //----------------------
            parentNode.right = insertR(key, value, parentNode.right);
        }
        else {  // Ansonsten Fall: Wenn der Schlüssel bereits vorhanden ist
            oldValue = parentNode.value;
            parentNode.value = value;
        }
        parentNode = balance(parentNode); //Um AVL Struktur zu erhalten
        return parentNode;

    }


    //Hilfsmethode zum entfernen eines K,V- Paares
    public V remove(K key){
        rootNode = removeR(key, rootNode);
        return oldValue;
    }

    // remove-Methode
    private Node<K, V> removeR(K key, Node<K, V> parentNode) {
        //Es sind 4 Fälle zu unterscheiden: leer, wurzelknoten, 1 Kind, 2 Kind

        if(parentNode == null){
            oldValue = null;
            //return null;
        }
        else if(key.compareTo(parentNode.key) < 0){
            parentNode.left = removeR(key, parentNode.left);
            if(parentNode.left != null){
                parentNode.left.parent = parentNode;
            }
        }
        else if (key.compareTo(parentNode.key) > 0){
            parentNode.right = removeR(key, parentNode.right);
            if(parentNode.right != null){
                parentNode.right.parent = parentNode;
            }
        }
        //Hat 1 oder kein Kind, kritisch, denn wir müssen umabuen
        else if(parentNode.left == null || parentNode.right == null){
            //parentNode muss gelöscht bzw. umgesetzt werden
            oldValue = parentNode.value;


            //Macht Sinn, denn die Seite, welche einen Wert
            // enthält wird zum neuen parentNode
            Node<K,V> gp = parentNode.parent;
            parentNode = (parentNode.left != null) ? parentNode.left : parentNode.right;    //Ist das hier eine if()-Abfrage? Noch nie gesehen
            if(parentNode != null){
                parentNode.parent = gp;
            }
            size--;
        }
        else {
            //parentNode hat zwei Kinder und muss gelöscht werden bzw. umgeschrieben

            //MinEntry ist ein Hilfsdatentyp für den Rückgabeparameter von getRemMinR
            MinEntry<K,V> min = new MinEntry<K,V>();
            parentNode.right = getRemMinR(parentNode.right, min);
            if(parentNode.right != null){
                parentNode.right.parent = parentNode;
            }
            oldValue = parentNode.value;
            parentNode.key = min.key;
            parentNode.value = min.value;

            size--;
        }
        parentNode = balance(parentNode); //Um AVL Struktur zu erhalten
        return parentNode;

    }

    //Diese Methode sucht bei 2 Kindern, den minimalen Knoten der Dreien.
    //Löscht den kleinsten Wert und hängt den Elternknoten korrekt um.
    // Ausserdem liefert Daten des gelöschten Knotens über MinEntry zurück.
    private Node<K,V> getRemMinR(Node<K,V> parentNode, MinEntry<K,V> min){
        //Annahme parentNode != null
        assert parentNode != null;
        if(parentNode.left == null){
            min.key = parentNode.key;
            min.value = parentNode.value;
            parentNode = parentNode.right;
        }
        else {
            parentNode.left = getRemMinR(parentNode.left, min);
        }
        parentNode = balance(parentNode); //Um AVL Struktur zu erhalten
        return parentNode;

    }


    private static class MinEntry<K, V>{
        private K key;
        private V value;
    }

    // Jetzt noch den Iterator für den BST
    // Aktualisierung: Iteratoren in einem BST mit InOrder-Traversierung nicht effizient --> daher nicht machbar
        private Node<K,V> leftMostDescendant(Node<K,V> parentNode){
            assert parentNode != null;
            while(parentNode.left != null){
                parentNode = parentNode.left;
            }
            return parentNode;
        }

        private Node<K,V> parentOfLeftMostAncestor(Node<K,V> parentNode){
            //prüfe auf Leerheit
            assert parentNode != null;
            while(parentNode.parent != null && parentNode.parent.right == parentNode){
                parentNode = parentNode.parent;
            }
            return parentNode.parent;   //kann auch null sein
        }

        @Override()
        public Iterator<Entry<K, V>> iterator() {
            return new BTDIterator();
        }

        public class BTDIterator implements Iterator<Entry<K,V>> {

            public BTDIterator(){
                if(rootNode != null) parentNode = leftMostDescendant(rootNode);
            }



            // Erster Knoten:
            Node<K,V> parentNode = null;

            @Override
            public boolean hasNext() {
                return parentNode != null;
            }

            @Override
            public Entry<K, V> next() {
                Node<K,V> pNode = parentNode;

                if(parentNode.right != null){
                    parentNode = leftMostDescendant(parentNode.right);
                }
                else{
                    parentNode = parentOfLeftMostAncestor(parentNode);
                }

                return new Entry<K,V>(pNode.key, pNode.value);
            }
        }


    /**
	 * Pretty prints the tree
	 */
	public void prettyPrint() {
        printR(0, rootNode);
    }

    private void printR(int level, Node<K, V> parentNode) {
        printLevel(level);
        if (parentNode == null) {
            System.out.println("#");
        } else {
            System.out.println(parentNode.key + " " + parentNode.value + "^" + ((parentNode.parent == null) ? "null" : parentNode.parent.key.toString()));
            if (parentNode.left != null || parentNode.right != null) {
                printR(level + 1, parentNode.left);
                printR(level + 1, parentNode.right);
            }
        }
    }

    private static void printLevel(int level) {
        if (level == 0) {
            return;
        }
        for (int i = 0; i < level - 1; i++) {
            System.out.print("   ");
        }
        System.out.print("|__");
    }

    @Override
    public int size() {
        return size;
    }

     // Ausbalancieren ab hier
     // erweiterung für AVL-Bäume

     private int getHeight(Node<K,V> p){
        if(p == null){
            return -1;
        }
        else{
            return p.height;
        }
     }

    private int getBalance(Node<K,V> p){
        if(p == null){
            return 0;
        }
        else{
            return getHeight(p.right) - getHeight(p.left);
        }
    }

    private Node<K,V> rotateLeft(Node<K,V> p){
        assert p.right != null;
        Node<K,V> q = p.right;//Pro Zeile wird ein neur Zeiger gesetzt und immmer
        p.right = q.left;   // der Zeiger wieder aufgenommen, welcher in der vorherigen Zeile benutzt wurde

        //Veränderter Part
        if(p.right != null)
            p.right.parent = p;
        q.left = p;
        if(q.left != null)
            q.left.parent = q;

        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;

        return q;

    }


    private Node<K,V> rotateRight(Node<K,V> p){
        assert p.left != null;
        Node<K,V> q = p.left;
        p.left = q.right;
        q.right = p;
        p.height = Math.max(getHeight(p.left),getHeight(p.right)) +1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) +1;

        return q;
    }

    //rotateLeftRight ist eine Kombination aus zwei vorherigen Methoden
    private Node<K,V> rotateLeftRight(Node<K,V> p){
        assert p.left != null;
        p.left = rotateLeft(p.left);

        //Neu
        if(p.left != null){
            p.left.parent = p;
        }

        return rotateRight(p);

    }

    private Node<K,V> rotateRightLeft(Node<K,V> p){
        assert p.right != null;
        p.right = rotateRight(p.right);

        if(p.right != null){
            p.right.parent = p;
        }

        return rotateLeft(p);
    }


    //Mit der balanceMethode moechten wir den Binären Suchbaum auf eine
    // maximale Hoehe von 2 bringen, mit Hilfe der AVL-Baum Eigenschaft
    private Node<K,V> balance(Node<K,V> p){
        if(p == null){
            return null;
        }
        p.height = Math.max(getHeight(p.left), getHeight(p.right))+1;//Höhe appktualisieren

        //ifAbfrage nach der aktuellen p höhe
        if(getBalance(p) == -2){
            if(getBalance(p.left)<= 0){
                //Fall A1
                p = rotateRight(p);
            }
            else{
                //Fall A2
                p = rotateLeftRight(p);
            }
        }
        else if(getBalance(p) == +2){
            //Fall B1
            if(getBalance(p.right) >= 0){
                p = rotateLeft(p);
            }
            else{
                p = rotateRightLeft(p);
            }
        }

        return p;
    }


}
