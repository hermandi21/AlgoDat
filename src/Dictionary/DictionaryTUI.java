
package Dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.swing.JFileChooser;

public class DictionaryTUI{

    private static SortedMap<String, String> dictionary;

    //Konstruktor
    public DictionaryTUI(){

        dictionary = new TreeMap<>();
    }

    public static void main(String[] args) {
        System.out.println("Wilkommen in der TUI für das Dictionary");
        System.out.println("Bitte geben Sie ein Kommando ein: (create, r, p, s, i, d, exit)");

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while(!exit){
            System.out.println("> ");
            String command = scanner.next();    //command ist der eingegebene Befehl

            switch(command.toLowerCase()){

                case "create":
                    createDictionary(scanner);
                    break;
                case "r":
                    readFromFile(scanner);
                    break;
                case "p":
                    printDictionary();
                    break;
                case "s":
                    searchWord();
                    break;
                case "i":
                    insertWord(scanner);
                    break;
                case "d":
                    deleteWord(scanner);
                    break;
                case "exit":
                    exit = true;
                    System.out.println("Programm beendet");
                    break;

                default: 
                    System.out.println("Ungueltiges Kommando, bitte nochmal versuchen");
            }
        }
    }

    private static void createDictionary(Scanner scanner) {
        System.out.println("Bitte geben Sie die gewünschte Implementierung an (SortedArrayDictionary, HashDictionary, BinaryTreeDictionary):");
        String implementation = scanner.next();
        switch (implementation.toLowerCase()) {
            case "sortedarraydictionary":
                dictionary = new SortedArrayDictionary<>();
                System.out.println("Dictionary mit SortedArrayDictionary erstellt.");
                break;
            case "hashdictionary":
                dictionary = new HashDictionary<>(3);
                System.out.println("Dictionary mit HashDictionary erstellt.");
                break;
             /* case "binarytreedictionary":
                dictionary = new BinaryTreeDictionary<>();
                System.out.println("Dictionary mit BinaryTreeDictionary erstellt.");
                break;  */
            default:
                System.out.println("Ungültige Implementierung! Es wurde SortedArrayDictionary verwendet.");
                dictionary = new SortedArrayDictionary<>();
                break;
        }
    }

    // Methode zum Hinzufügen von Wortpaaren
    public void insertWord(Scanner scanner) {
        System.out.print("Deutsches Wort: ");
        String deutsch = scanner.nextLine();
        System.out.print("Englisches Wort: ");
        String englisch = scanner.nextLine();
        dictionary.put(deutsch, englisch);
        System.out.println("Wortpaar hinzugefügt: " + deutsch + " - " + englisch);
    }

    // Methode zum Löschen eines Wortpaars
    public static void deleteWord(Scanner scanner) {
        System.out.print("Deutsches Wort: ");
        String deutsch = scanner.nextLine();
        if (dictionary.containsKey(deutsch)) {
            String englisch = dictionary.remove(deutsch);
            System.out.println("Wortpaar gelöscht: " + deutsch + " - " + englisch);
        } else {
            System.out.println("Das deutsche Wort \"" + deutsch + "\" ist nicht im Wörterbuch enthalten.");
        }
    }

    // Methode zum Suchen eines englischen Wortes
    public static void searchWord(Scanner scanner) {
        System.out.print("Deutsches Wort: ");
        String deutsch = scanner.nextLine();
        if (dictionary.containsKey(deutsch)) {
            String englisch = dictionary.get(deutsch);
            System.out.println("Englisches Wort: " + englisch);
        } else {
            System.out.println("Das deutsche Wort \"" + deutsch + "\" ist nicht im Wörterbuch enthalten.");
        }
    }

    // Methode zum Ausgeben aller Einträge des Wörterbuchs
    public static void printDictionary() {
        if (dictionary.isEmpty()) {
            System.out.println("Das Wörterbuch ist leer.");
        } else {
            System.out.println("Wörterbuch:");
            for (Entry<String, String> entry : dictionary.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
        }
    }

    // Methode zum Einlesen von Einträgen aus einer Datei
    public static void readFromFile(Scanner scanner) {
        System.out.print("Dateiname: ");
        String dateiname = scanner.nextLine();
        File file = new File(dateiname);
        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] words = line.split(" - ");
                if (words.length == 2) {
                    dictionary.put(words[0], words[1]);
                }
            }
            fileScanner.close();
            System.out.println("Einträge aus der Datei eingelesen: " + dictionary.size() + " Einträge");
        } catch (FileNotFoundException e) {
            System.out.println("Datei nicht gefunden: " + dateiname);
        }
    }
    
}
 