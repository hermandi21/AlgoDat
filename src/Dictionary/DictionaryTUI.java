
package Dictionary;

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

        for(;;){
            String in = sc.nextLine();
            command(in);
        }
        
    }

    private static void command(String com) throws Exception
    {
        String args[] = com.split(" ");

        switch (args[0])
        {
            case "create":
                createDictionary(args);
                break;
            case "read":
                if (dictionaryExists())
                    readFromFile(Arrays.copyOfRange(args, 1, args.length));
                else
                    System.out.println("Dictionary doesn't exist");
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

    private static void searchWord(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("wrong command!");
            return;
        }

        System.out.println(dictionary.search(args[0]));
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

    private static void readFromFile(String[] args) throws Exception {
        if (args.length == 0 || args.length > 2) {
            System.out.println("wrong command!");
            return;
        }

        String filename = args.length == 2 ? args[1] : chooseFile();

        BufferedReader reader = null;
        String line;
        int counter = 0;    //counter dazu da um abzubrechen nach bspw. 10 Einträgen

        if (args.length == 1) { //args.length = 1, heißt es soll alle Einträge auslesen und ins dict schreiben

            reader = new BufferedReader(new FileReader(new File(filename)));

            while ((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                dictionary.insert(words[0], words[1]);
            }
        } else if (args.length == 2) {  //Wenn man den Dateinamen mit angibt soll das hier
            int n = Integer.parseInt(args[0]);
            reader = new BufferedReader(new FileReader(new File("/home/hermandi/Schreibtisch/hochschule/AIN3/ALDA/prog/aufgabe01/src/Dictionary/dtengl.txt")));

            
            while ((line = reader.readLine()) != null && counter < n) {
                String[] words = line.split(" ");
                dictionary.insert(words[0], words[1]);
                counter++;
            }
        }
        reader.close();
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
                dictionary = new HashDictionary<String, String>(3);
                System.out.println("HashDicitionary sucessfully created!");
                return;
            } else if (arg.equals("binarytreedictionary")){
                //dictionary = new BinaryTreeDictionary();
                System.out.println("BinaryTreeDictionary sucessfully created! \n");
                return;
            }
        }
        dictionary = new SortedArrayDictionary<String, String>();
        System.out.println("SortedArrayDictionary successfully created!");

    }

    private static boolean dictionaryExists(){
        return dictionary == null ? false : true;
    }

}
