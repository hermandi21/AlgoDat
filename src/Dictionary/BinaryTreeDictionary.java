package Dictionary;

public class BinaryTreeDictionary<K extends Comparable<? super K>,V> {

    //Klasse Node
    public static class Node<K,V>{
        private Node<K,V> parent;
        private K key;
        private V value;
        private Node<K,V> left;
        private Node<K,V> right;

        //Konstruktor des Knotens
        private Node(K k, V v){
            this.key = k;
            this.value = v;
            left = null;
            right = null;
            parent = null;
        }

    }
    //Anfangsdeklaration Wurzelknoten wird null gesetzt
    private Node<K,V> rootNode = null;

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
            oldValue = null;
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
        return parentNode;

    }


    private V oldValue2;

    //Hilfsmethode zum entfernen eines K,V- Paares
    public V remove(K key){
        rootNode = removeR(key, rootNode);
        return oldValue2;
    }

    // remove-Methode 
    private Node<K, V> removeR(K key, Node<K, V> parentNode) {
        //Es sind 4 Fälle zu unterscheiden: leer, wurzelknoten, 1 Kind, 2 Kind

        if(parentNode == null){oldValue2 = null;}
        else if(key.compareTo(parentNode.key) < 0){
            parentNode.left = removeR(key, parentNode.left);
        }
        else if (key.compareTo(parentNode.key) > 0){
            parentNode.right = removeR(key, parentNode.right);
        }
        //Hat 1 oder kein Kind, kritisch, denn wir müssen umabuen
        else if(parentNode.left == null || parentNode.right == null){
            //parentNode muss gelöscht bzw. umgesetzt werden
            oldValue2 = parentNode.value;
            
            //Macht Sinn, denn die Seite, welche einen Wert 
            // enthält wird zum neuen parentNode
            parentNode = (parentNode.left != null) ? parentNode.left : parentNode.right;    //Ist das hier eine if()-Abfrage? Noch nie gesehen
        }
        else {
            //parentNode hat zwei Kinder und muss gelöscht werden bzw. umgeschrieben

            //MinEntry ist ein Hilfsdatentyp für den Rückgabeparameter von getRemMinR
            MinEntry<K,V> min = new MinEntry<K,V>();
            parentNode.right = getRemMinR(parentNode.right, min);
            oldValue2 = parentNode.value;
            parentNode.key = min.key;
            parentNode.value = min.value;
        }
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
        while(parentNode != null){
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

    /*
        Verstehe nicht warum er mir in Zeile 186 ein Fehler wirft
        // Erster Knoten:
        Node<K,V> parentNode = null;

        if (rootNode != null){
            parentNode = leftMostDescendant(rootNode);
        }

        while(parentNode != null){
            System.out.print(parentNode.key + ", ");
            if(parentNode.right != null){
                parentNode = leftMostDescendant(parentNode.right);
            }
            else{
                parentNode = parentOfLeftMostAncestor(parentNode);
            }
        }

        */
}
