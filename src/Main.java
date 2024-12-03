import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<String> myArrList = new ArrayList<>();
    private static Scanner in = new Scanner(System.in);
    private static boolean needsToBeSaved = false;
    private static String currentFilename = null;

    public static void main(String[] args)
    {
        boolean done = false;
        do
        {
            displayMenu();
            String choice = SafeInput.getRegExString(in, "Choose an option: ", "[AaDdIiMmOoSsCcVvQq]");

           if (choice.equalsIgnoreCase("A"))
                    addItem();

           else if (choice.equalsIgnoreCase("D"))
                    deleteItem();

           else if (choice.equalsIgnoreCase("I"))
                    insertItem();

           else if (choice.equalsIgnoreCase("M"))
                    moveItem();

           else if (choice.equalsIgnoreCase("O"))
                    openList();

           else if (choice.equalsIgnoreCase("S"))
                    saveList();

           else if (choice.equalsIgnoreCase("C"))
                    clearList();

           else if (choice.equalsIgnoreCase("V"))
                    viewList();

           else if (choice.equalsIgnoreCase("Q"))
                    done = confirmExit();
           else
               System.out.println("Invalid input. Try again.");

        } while (!done);
    }

    private static void displayMenu()
    {
        System.out.println("\nMenu:");
        System.out.println("A - Add an item");
        System.out.println("D - Delete an item");
        System.out.println("I - Insert an item");
        System.out.println("M - Move an item");
        System.out.println("O - Open a list file from disk");
        System.out.println("S - Save the current list to disk");
        System.out.println("C - Clear the list");
        System.out.println("V - View the list");
        System.out.println("Q - Quit");
    }

    private static void addItem()
    {
        String newItem = SafeInput.getNonZeroLenString(in, "Enter a new item to add: ");
        myArrList.add(newItem);
        needsToBeSaved = true;
        System.out.println("Item added.");
    }

    private static void deleteItem()
    {
        if (myArrList.isEmpty())
        {
            System.out.println("The list is empty. No items to delete.");
            return;
        }

        viewList();
        int itemNumber = SafeInput.getRangedInt(in, "Enter the number of the item to delete: ", 1, myArrList.size());
        myArrList.remove(itemNumber - 1);
        needsToBeSaved = true;
        System.out.println("Item deleted.");
    }

    private static void insertItem()
    {
        if (myArrList.isEmpty())
        {
            System.out.println("The list is empty. Adding item at position 1.");
            addItem();
            return;
        }

        viewList();
        int position = SafeInput.getRangedInt(in, "Enter the position to insert the new item (1 to " + (myArrList.size() + 1) + "): ", 1, myArrList.size() + 1);
        String newItem = SafeInput.getNonZeroLenString(in, "Enter a new item to insert: ");
        myArrList.add(position - 1, newItem);
        needsToBeSaved = true;
        System.out.println("Item inserted.");
    }

    private static void moveItem()
    {
        if (myArrList.isEmpty())
        {
            System.out.println("The list is empty. No items to move.");
            return;
        }

        viewList();
        int fromIndex = SafeInput.getRangedInt(in, "Enter the number of the item to move: ", 1, myArrList.size()) - 1;
        int toIndex = SafeInput.getRangedInt(in, "Enter the new position for the item (1 to " + myArrList.size() + "): ", 1, myArrList.size()) - 1;

        String item = myArrList.remove(fromIndex);
        myArrList.add(toIndex, item);
        needsToBeSaved = true;
        System.out.println("Item moved.");
    }

    private static void openList()
    {
        if (needsToBeSaved)
        {
            boolean saveFirst = SafeInput.getYNConfirm(in, "The current list has unsaved changes. Do you want to save it first?");
            if (saveFirst)
                saveList();
        }

        String filename = SafeInput.getNonZeroLenString(in, "Enter the filename to open") + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            myArrList.clear();
            String line;
            while ((line = reader.readLine()) != null)
                myArrList.add(line);
            currentFilename = filename;
            needsToBeSaved = false;
            System.out.println("List loaded from " + filename);
        }
        catch (IOException e)
        {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private static void saveList()
    {
        if (currentFilename == null)
            currentFilename = SafeInput.getNonZeroLenString(in, "Enter a filename to save the list: ") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFilename))) {
            for (String item : myArrList) {
                writer.write(item);
                writer.newLine();
            }
            needsToBeSaved = false;
            System.out.println("List saved to " + currentFilename);
        }
        catch (IOException e)
        {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void clearList()
    {
        if (myArrList.isEmpty())
        {
            System.out.println("The list is already empty.");
            return;
        }

        boolean confirm = SafeInput.getYNConfirm(in, "Are you sure you want to clear the list?");
        if (confirm)
        {
            myArrList.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");
        }
    }

    private static void viewList()
    {
        if (myArrList.isEmpty())
            System.out.println("The list is empty.");

        else
        {
            for (int i = 0; i < myArrList.size(); i++)
                System.out.println((i + 1) + ". " + myArrList.get(i));
        }
    }

    private static boolean confirmExit()
    {
        if (needsToBeSaved)
        {
            boolean saveBeforeExit = SafeInput.getYNConfirm(in, "The current list has unsaved changes. Do you want to save before exiting?");
            if (saveBeforeExit)
                saveList();
        }
        return SafeInput.getYNConfirm(in, "Are you sure you want to quit?");
    }
}
