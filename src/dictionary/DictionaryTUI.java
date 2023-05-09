
package dictionary;

import javax.swing.*;
import java.beans.PropertyEditorSupport;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class DictionaryTUI{

    private static Dictionary<String, String> dictionary = new SortedArrayDictionary();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while(true) {
            String command = sc.nextLine();
            parseCommand(command);
        }
    }

    private static void parseCommand(String cmd) {
        String[] command = cmd.split(" ");

        switch (command[0]) {
            case "create":
                if (command.length == 1)
                    create(null);
                else
                    create(command[1]);
                break;
            case "read":
                if (command.length == 1)
                    read(0);
                else if (command.length >= 2)
                    read(Integer.parseInt(command[1]));
                else
                    System.out.println("Eingabe im Format r [n] Dateiname");
                break;
            case "p":
                for (Dictionary.Entry<String, String> e : dictionary) {
                    System.out.println(e.getKey() + ": " + e.getValue() + " search: " + dictionary.search(e.getKey()));
                }
                break;
            case "s":
                if (command.length >= 2) {
                    String searchedValue = dictionary.search(command[1]);
                    if (searchedValue == null) {
                        System.out.println("Nicht gefunden");
                    } else {
                        System.out.println(dictionary.search(command[1]));
                    }
                }
                else
                    System.out.println("Eingabe im Format s deutsch");

                break;
            case "i":
                if (command.length == 3) {
                    String oldValue = dictionary.insert(command[1], command[2]);
                    if (oldValue == null) {
                        System.out.println(command[1] + " eingefügt");
                    } else {
                        System.out.println(oldValue + " in " + command[2] + " geändert");
                    }
                }
                else
                    System.out.println("Eingabe im Format i deutsch englisch");
                break;
            case "d":
                if (command.length >= 2) {
                    String deletedValue = dictionary.remove(command[1]);
                    if (deletedValue == null) {
                        System.out.println("Nichts zu löschen");
                    } else {
                        System.out.println(deletedValue + " gelöscht");
                    }
                }
                else
                    System.out.println("Eingabe im Format d deutsch");
                break;
            case "exit":
                System.out.println("Programm beendet");
                System.exit(0);
            case "searchAll":
                long startTime = System.nanoTime();
                for (Dictionary.Entry<String, String> e : dictionary) {
                    parseCommand("s " + e.getKey());
                }
                long endTime = System.nanoTime();
                long timeTaken = (endTime - startTime) / 1000000;
                System.out.println("Dauer: " + timeTaken + "ms");
                break;
            case "searchAllE":
                long startTTime = System.nanoTime();
                for (Dictionary.Entry<String, String> e : dictionary) {
                    parseCommand("search " + e.getValue());
                }
                long endTTime = System.nanoTime();
                long timeTTaken = (endTTime - startTTime) / 1000000;
                System.out.println("Dauer: " + timeTTaken + "ms");
                break;
        }
    }

    private static void create(String impl) {
        if (impl == null) {
            dictionary = new SortedArrayDictionary<>();
            System.out.println("SortedArrayDictionary erstellt");
        }
        else if (impl.equals("b")) {
            dictionary = new BinaryTreeDictionary<>();
            System.out.println("BinaryTreeDictionary erstellt");
        }
        else if (impl.equals("h")) {
            dictionary = new HashDictionary<>(3);
            System.out.println("HashDictionary erstellt");
        }
        else if (impl.equals("s")) {
            dictionary = new SortedArrayDictionary<>();
            System.out.println("SortedArrayDictionary erstellt");
        }
        else
            System.out.println("Eingabe im Format create s|b|h");
    }

    private static void read(int n) {
        // JFileChooser-Objekt erstellen
        JFileChooser chooser = new JFileChooser();

        int rueckgabeWert = chooser.showOpenDialog(null);

        /* Abfrage, ob auf "Öffnen" geklickt wurde */
        if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
            try {
                long startTime = System.nanoTime();
                BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()));
                if (n == 0) {
                    while(true) {
                        String line = reader.readLine();
                        if (line != null) {
                            String[] lineArr = line.split(" ");
                            dictionary.insert(lineArr[0], lineArr[1]);
                            continue;
                        }
                        break;
                    }
                    long endTime = System.nanoTime();
                    long timeTaken = (endTime - startTime) / 1000000;
                    System.out.println("Datei eingelesen; Dauer: " + timeTaken + "ms");
                } else {
                    for (int i = 0; i < n; i++) {
                        String line = reader.readLine();
                        if (line != null) {
                            String[] lineArr = line.split(" ");
                            dictionary.insert(lineArr[0], lineArr[1]);
                            continue;
                        }
                        break;
                    }
                    long endTime = System.nanoTime();
                    long timeTaken = (endTime - startTime) / 1000000;
                    System.out.println("Datei eingelesen; Dauer: " + timeTaken + "ms");
                }
                //System.out.println(dictionary);
            } catch (FileNotFoundException fe) {
                System.out.println("File not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
