import TreePackage.*;
import java.io.*;
import java.util.*;

public class Identifiers
{
    public static void main(String args[])
    {
        String fileName = getFileName();
        System.out.println();

        BinarySearchTree<String> unique = getPossibleIds(fileName);

        // Print all identifiers in sorted (inorder) order
        System.out.println("Identifiers found in file (sorted):");
        System.out.println("------------------------------------");

        Iterator<String> iter = unique.getInorderIterator();
        while (iter.hasNext())
        {
            System.out.println(iter.next());
        }

        System.out.println("------------------------------------");
        System.out.println("Total unique identifiers: " + unique.getNumberOfNodes());
    }

    /**
     * Get the possible identifiers from the file.
     * A valid identifier starts with a letter or underscore,
     * and contains only letters, digits, and underscores.
     *
     * @return A tree of possible identifiers from the file.
     */
    private static BinarySearchTree<String> getPossibleIds(String theFileName)
    {
        Scanner input;
        BinarySearchTree<String> possible = new BinarySearchTree<String>();

        try
        {
            input = new Scanner(new File(theFileName));

            // Read each token from the file
            while (input.hasNext())
            {
                String token = input.next();

                // Strip any leading/trailing punctuation (e.g. commas, semicolons)
                token = token.replaceAll("^[^a-zA-Z_]+|[^a-zA-Z0-9_]+$", "");

                // Check if the token is a valid identifier:
                // - Not empty
                // - Starts with a letter or underscore
                // - Contains only letters, digits, and underscores
                if (!token.isEmpty() && token.matches("[a-zA-Z_][a-zA-Z0-9_]*"))
                {
                    possible.add(token); // Duplicates are handled by BST (ignored)
                }
            }

            input.close();
        }
        catch (IOException e)
        {
            System.out.println("There was an error reading the file.");
            System.out.println(e.getMessage());
        }

        return possible;
    }

    private static String getFileName()
    {
        Scanner input;
        String inString = "data.txt";

        try
        {
            input = new Scanner(System.in);

            System.out.println("Please enter the name of the file:");
            inString = input.next();
        }
        catch (Exception e)
        {
            System.out.println("There was an error with System.in");
            System.out.println(e.getMessage());
            System.out.println("Will try the default file name data.txt");
        }

        return inString;
    }
}