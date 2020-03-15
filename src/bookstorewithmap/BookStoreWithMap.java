/*
 *              Revision History
 * ***************************************************************
 * 10/17/19 - Worked on lab - Thomas Bahun
 * 2019 - revised for use in a lab.
 */
package bookstorewithmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author aapplin
 */
public class BookStoreWithMap {

    private Map <String, Book> inventory = new TreeMap<>();
    /**
     * readInventory reads the entire inventory file into an ArrayList,
     * and then sorts it using Collections.sort()
     * @param fileName the first command line argument
     */
    public void readInventory(String fileName) {
        try {
            Scanner inFile = new Scanner(new FileReader(fileName));

            if (!inFile.hasNext()) {
                System.out.println("The file is empty.");
                System.exit(1);
            }
            // if there is stuff in the file... continue
            String isbn;
            int numOnShelf;
            while (inFile.hasNext()) {
                isbn = inFile.next();
                numOnShelf = inFile.nextInt();
                if (isbn.length() == 13 && numOnShelf >= 0) {
                    inventory.put(isbn,new Book(isbn, numOnShelf));
                }
            }
            inFile.close();
        } catch (FileNotFoundException ex) {
            System.err.println("The file " + fileName + " does not exist");
            System.exit(1);
        } catch (InputMismatchException ex) {
            System.err.println("Attempt to read the wrong data type.");
            System.exit(1);
        }

    }
   
    /**
     * Update inventory reads a file of new deliveries to the book store.
     * if the book is already in inventory it updates the number on shelf
     * by the number delivered.  If it is not in inventory it is added.
     * @param fileName a command line argument 
     */
    public void updateInventory(String fileName) {
        try {
            Scanner inFile = new Scanner(new FileReader(fileName));

            if (!inFile.hasNext()) { // nothing in the file?? 
                System.out.println("The file is empty.");
                System.exit(1);
            }
            // if there is stuff in the file... continue
            String isbn;
            int numInShipment;
            while (inFile.hasNext()) {
                isbn = inFile.next();
                numInShipment = inFile.nextInt();
                if (isbn.length() == 13 && numInShipment >= 0) { // valid
                    if (inventory.containsKey(isbn) ) { // we carry this book
                        Book b = inventory.get(isbn);
                        b.addToNumberOnShelf(numInShipment);
                        inventory.put(isbn, b);
                    } else { // this is a new book
                        inventory.put(isbn, new Book(isbn, numInShipment));
                    }
                }
            }
            inFile.close();
        } catch (FileNotFoundException ex) {
            System.err.println("The file " + fileName + " does not exist");
            System.exit(1);
        } catch (InputMismatchException ex) {
            System.err.println("Attempt to read the wrong data type.");
            System.exit(1);
        }
    }
    /**
     * writeNewInventory writes the current inventory to a file.
     * @param fileName  a command line parameter
     */
    public void writeNewInventory(String fileName) {
        try {
            PrintStream out = new PrintStream(new File(fileName));
            for (String isbn : inventory.keySet()){
                out.println(inventory.get(isbn));
            }
            out.close();
        }catch(FileNotFoundException ex){
            System.out.println("The file or directory is write protected");
            System.exit(1);
        }
    }

    /**
     * the driver for the program
     * @param filenames the command line arguments (3 are required for this
     * application)
     */
    public void run(String[] filenames) {
        readInventory(filenames[0]);
        System.out.println("Number of books in inventory " + inventory.size());
        updateInventory(filenames[1]);
        System.out.println("Number of books in inventory after update "
                + inventory.size());
        writeNewInventory(filenames[2]);
    }

    /**
     * This method checks for the correct number of command line arguments and
     * exits if they are not there. This application needs 3 filenames
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("usage: inFile updateFile outFile");
            System.exit(1);
        }
        BookStoreWithMap driver = new BookStoreWithMap();
        driver.run(args);
    }
}
