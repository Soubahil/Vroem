package be.intecbrussel.services;

import java.util.Scanner;

public class SharedService {
    private final Scanner scanner;

    public SharedService(){
        scanner = new Scanner(System.in);
    }

    public boolean yesNo() {
        String input;
        boolean keepOnGoing = true;
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("yes"))
                keepOnGoing = true;
            else if(input.equalsIgnoreCase("no")) {
                keepOnGoing = false;
            }
            else
                System.err.println(input + " is not a good answer, try again.");
        } while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));
        return keepOnGoing;
    }
}
