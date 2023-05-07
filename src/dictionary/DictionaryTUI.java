
package dictionary;

import javax.swing.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class DictionaryTUI{

    private static Dictionary<String, String> dictionary;

    public static void main(String[] args) throws Exception{
        System.out.println("German - English Dictionary v.1.2\n");
        Scanner sc = new Scanner(System.in);

        System.out.println("Willkommen in der TUI für das Dictionary");
        System.out.println("Bitte geben Sie ein Kommando ein: (create, read, p, s, i, d, exit)");

        while (true){
            String in = sc.nextLine();
            command(in);
        }
        
    }

    private static void command(String com) throws Exception
    {
        String[] args = com.split(" ");

        switch (args[0])
        {
            case "create":
                createDictionary(args);
                break;
            case "read":
                if (args.length == 1)
                    readFromFile(0);
                else if (args.length >= 2)
                    readFromFile(Integer.parseInt(args[1]));
                else
                    System.out.println("Eingabe im Format r [n] Dateiname");
                break;
            case "p":
                if (dictionaryExists())
                    printDictionary();
                else
                    System.out.println("Dictionary doesn't exist");
                break;
            case "s":
                if (dictionaryExists())
                    searchWord(Arrays.copyOfRange(args, 1, args.length));
                else
                    System.out.println("Dictionary doesn't exist");
                break;
            case "i":
                if (dictionaryExists())
                    insertWord(Arrays.copyOfRange(args, 1, args.length));
                else
                    System.out.println("Dictionary doesn't exist");
                break;
            case "d":
                if (dictionaryExists())
                    deleteWord(Arrays.copyOfRange(args, 1, args.length));
                else
                    System.out.println("Dictionary doesn't exist");
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                System.out.println("wrong command!");
                break;
        }
    }



    private static void deleteWord(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("wrong command!");
            return;
        }

        dictionary.remove(args[0]);
    }

    private static void insertWord(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("wrong command!");
            return;
        }

        dictionary.insert(args[0], args[1]);
    }

    //Ich gehe hier davon aus wenn ich nach einem Wort suche welches
    // nicht enthalten ist, durchsucht es dennoch die ganze Liste
    //Somit habe ich den Zeitwert genommen bei einem nichte enthaltenen Wort
    private static void searchWord(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("wrong command!");
            return;
        }
        double time1 = System.nanoTime();
        System.out.println(dictionary.search(args[0]));
        
        
        double time2 = System.nanoTime();
        double diff =  (time2 - time1)/1.0e06;
        System.out.println("Das Wort wurde in " + diff + " ms gefunden!");
    }

    //Methode um die einzelnen Wörter im dict zu drucken
    private static void printDictionary()
    {
        if (dictionary != null) {
            System.out.println("Wörterbuch:");
            for (Dictionary.Entry<String, String> entry : dictionary) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        } else {
            System.out.println("Es existiert noch kein Wörterbuch. Bitte erstellen Sie zuerst ein Wörterbuch.");
        }
    }

        private static void readFromFile(int n) {
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

    private static String chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        } else {
            System.out.println("No file selected.");
            return null;
        }
    }


    private static void createDictionary(String[] args) throws Exception
    {
        
        if (args.length >= 2)
        {
            // to lower case, that command becomes case insensitive
            String arg = args[1].toLowerCase();
            if (arg.equals("hashdictionary")){
                dictionary = new HashDictionary<>(3);
                System.out.println("HashDictionary sucessfully created!");
                return;
            } else if (arg.equals("binarytreedictionary")){
                dictionary = new BinaryTreeDictionary<>();
                System.out.println("BinaryTreeDictionary sucessfully created! \n");
                return;
            }
        }
        dictionary = new SortedArrayDictionary<>();
        System.out.println("SortedArrayDictionary successfully created!");
    }

    private static boolean dictionaryExists(){
        return dictionary != null;
    }

}
