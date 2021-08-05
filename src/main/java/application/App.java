package application;

import service.ProductLineService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    private static int choiceOne= 9;
    private static int choiceTwo= 9;
    private static boolean continueThis = true;

    public static void main(String[] args) {
        try {
            while (continueThis){
                getChoice();
                choices();
            }
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public static void getChoice(){
        Scanner scanner = new Scanner(System.in);
        while (choiceOne == 9) {
            System.out.println("~~~ VROEM ~~~ \nWhat do you want to look at? \n1. Product lines \n2. Products \n0. Exit");
            do {
                try {
                    choiceOne = scanner.nextInt();
                    if(choiceOne < 0 || choiceOne > 2){
                        choiceOne = 9;
                        System.err.println("Invalid choice, try again. (0 to exit)");
                    }
                } catch (InputMismatchException e) {
                    System.err.println("Invalid choice, try again. (0 to exit)");
                }
                scanner.nextLine();
            } while (choiceOne == 9);
            if(choiceOne == 0)
                break;
            else {
                while (choiceOne == 1 && choiceTwo == 9) {
                    System.out.println("~~~ Product Lines ~~~ \nWhat do you want to do? \n1. Show all product lines. \n2. Show product line by name. \n3: Add product line. \n4: Edit product line. \n5. Remove product line.\n0: Return to main menu.");
                    do {
                        try {
                            choiceTwo = scanner.nextInt();
                            if(choiceTwo < 0 ||choiceTwo > 5){
                                choiceTwo = 9;
                                System.err.println("Invalid choice, try again. (0 to return to main menu)");
                            }
                        } catch (InputMismatchException e) {
                            System.err.println("Invalid choice, try again. (0 to return to main menu)");
                        }
                        scanner.nextLine();
                    } while (choiceTwo == 9);
                    if (choiceTwo == 0)
                        break;
                }
            }
        }
    }

    private static void choices() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        String input;
        Scanner scanner = new Scanner(System.in);
        ProductLineService productLineService = new ProductLineService();
        if (choiceTwo != 0) {
            if (choiceOne == 1) {
                switch (choiceTwo) {
                    case 1 -> productLineService.showAllProductLines();
                    case 2 -> productLineService.showProductLineByName();
                    case 3 -> productLineService.createProductLine();
                    case 4 -> productLineService.updateProductLine();
                    case 5 -> productLineService.removeProductLine();
                }
            } else if (choiceOne == 0) {
                continueThis = false;
                System.out.println("Goodbye, have a nice day!");
                return;
            }
            System.out.println("Do you want to try again? Yes/No");
            do {
                input = scanner.nextLine();
                if (input.equalsIgnoreCase("yes")) {
                    return;
                }
                else if(input.equalsIgnoreCase("no")) {
                    choiceOne = 9;
                    choiceTwo = 9;
                }
                else
                    System.out.println(input + " is not a good answer,try again.");
            } while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));
        }
        if (choiceTwo == 0) {
            choiceOne = 9;
            choiceTwo = 9;
        }
    }
}
