package service;

import data.ProductLineDAO;
import model.ProductLine;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductLineService {

    private final Scanner scanner;
    private final ProductLineDAO productLineDAO;
    private final SharedService sharedService;

    public ProductLineService() {
        scanner = new Scanner(System.in);
        productLineDAO = new ProductLineDAO();
        sharedService = new SharedService();
    }

    public void showAllProductLines() {
        if (!Objects.requireNonNull(productLineDAO.getAllProductLines()).isEmpty())
            productLineDAO.getAllProductLines().forEach(System.out::println);
        else {
            System.out.println("No product lines to show. \nDo you want to create a new product line? Yes/No");
            if (sharedService.yesNo())
                createProductLine();
        }
    }

    public void showProductLineByName() {
        if (!Objects.requireNonNull(productLineDAO.getAllProductLines()).isEmpty()) {
            System.out.println("Insert the name that is linked to the product line: \n- X to show all product lines. \n- 0 to cancel.");
            while (true) {
                String input = scanner.nextLine();
                ProductLine productLine = productLineDAO.getProductLineByName(input);
                if(productLine != null) {
                    System.out.println(productLine);
                    break;
                } else if (input.equalsIgnoreCase("x")) {
                    showAllProductLines();
                    System.out.println();
                    System.out.println("Insert the name that is linked to the product line: \n- X to show all product lines. \n- 0 to cancel.");
                } else if (input.equals("0")) {
                    System.out.println("~ ACTION CANCELLED ~");
                    break;
                } else System.err.println("No product line linked to that name, try again. (X to show all product lines - 0 to cancel)");
            }
        } else {
            System.out.println("No product lines to show. \nDo you want to create a new product line? Yes/No");
            if (sharedService.yesNo())
                createProductLine();
        }
    }

    public void createProductLine() {
        String productLine = setProductLine();
        if (productLine.equals("0"))
            return;
        String textDescription = setTextDescription();
        if (textDescription.equals("0"))
            return;
        String htmlDescription = setHtmlDescription();
        if (htmlDescription.equals("0"))
            return;
        productLineDAO.addProductLine(new ProductLine(productLine, textDescription, htmlDescription));
        System.out.println("~ PRODUCT LINE CREATED ~");
    }

    public void updateProductLine() {
        if (!Objects.requireNonNull(productLineDAO.getAllProductLines()).isEmpty()) {
            System.out.println("Insert the name of the product line that you want to edit: \n- X to show all product lines. \n- 0 to cancel.");
            while (true) {
                String input = scanner.nextLine();
                ProductLine productLine = productLineDAO.getProductLineByName(input);
                boolean keepOnGoing;
                if (input.equals("0")) {
                    System.out.println("~ ACTION CANCELLED ~");
                    break;
                } else if (input.equalsIgnoreCase("x")) {
                    showAllProductLines();
                    System.out.println();
                    System.out.println("Insert the name of the product line that you want to edit: \n- X to show all product lines. \n- 0 to cancel.");
                } else if (productLine != null) {
                    System.out.println("What do you want to edit? \n1. Change the name of the product line. \n2. Change the description of the product line. \n3. Change the html description. \n0. Cancel");
                    do {
                        try {
                            int choice = scanner.nextInt();
                            if (choice == 1) {
                                setProductLine();
                                break;
                            } else if (choice == 2) {
                                setTextDescription();
                                break;
                            } else if (choice == 3) {
                                setHtmlDescription();
                                break;
                            } else if (choice == 0) {
                                scanner.nextLine();
                                System.out.println("~ ACTION CANCELLED ~");
                                break;
                            } else
                                System.err.println("Invalid choice, try again. (0 to cancel)");
                        } catch (InputMismatchException e) {
                            System.err.println("Invalid choice, try again. (0 to cancel)");
                        }
                        scanner.nextLine();
                        System.out.println("Do you want to continue editing this product line? Yes/No");
                        keepOnGoing = sharedService.yesNo();
                    } while (keepOnGoing);
                } else System.err.println("No product line linked to that name, try again. (X to show all product lines - O to cancel)");
            }
        } else {
            System.out.println("No product lines to edit. \nDo you want to create a new product line? Yes/No");
            if (sharedService.yesNo())
                createProductLine();
        }
    }

    public void removeProductLine(){
        if (!Objects.requireNonNull(productLineDAO.getAllProductLines()).isEmpty()) {
            System.out.println("Insert the name of the product line that you want to remove: \n- X to show all product lines. \n- 0 to cancel. ");
            while(true) {
                String input = scanner.nextLine();
                ProductLine productLine = productLineDAO.getProductLineByName(input);
                if(productLine != null) {
                    System.out.println("Are you sure you want to remove this product line: " + productLine.getProductLine() + "? Yes/No");
                    boolean areYouSure = sharedService.yesNo();
                    if (areYouSure) {
                        productLineDAO.deleteProductLine(productLine);
                        System.out.println("~ PRODUCT LINE: '" + productLine.getProductLine() + "' REMOVED ~");
                    } else
                        System.err.println("~ PRODUCT LINE: '" + productLine.getProductLine() + "' NOT REMOVED");
                    break;
                } else if (input.equals("0")) {
                    System.out.println("~ ACTION CANCELLED ~");
                    break;
                } else if (input.equalsIgnoreCase("x")) {
                    showAllProductLines();
                    System.out.println();
                    System.out.println("Insert the name of the product line that you want to remove: \n- X to show all product lines. \n- 0 to cancel. ");
                } else System.err.println("No product line linked to that name, try again. (X to show all product lines - O to cancel)");
            }
        } else {
            System.out.println("No product lines to remove. \nDo you want to create a new product line? Yes/No");
            if (sharedService.yesNo())
                createProductLine();
        }
    }

    //

    public boolean lacksRequirementsUserName(String productLine) {
        boolean lacksReq = true;
        if(productLine.length() >= 2 && productLine.length() >= 50) {
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~]");
            Matcher hasLetter = letter.matcher(productLine);
            Matcher hasSpecial = special.matcher(productLine);
            if(hasLetter.find(2))
                lacksReq = false;
            if(hasSpecial.find())
                lacksReq = true;
        }
        return lacksReq;
    }

    public String setProductLine() {
        String productLine;
        boolean isUnique;
        System.out.println("Insert the name that you want to use for your product line: \n- Between 2 and 50 characters. \n- Must have at least 2 alphabetic characters. \n- Can't contain special characters (except '-'). \n- 0 to cancel. ");
        do {
            productLine = scanner.nextLine();
            isUnique = true;
            if (productLine.equals("0")) {
                System.out.println("~ ACTION CANCELLED ~");
                break;
            }
            if (lacksRequirementsUserName(productLine))
                System.err.println("Product line name doesn't meet the requirements, try again. (0 to cancel)");
            for (ProductLine pl : productLineDAO.getAllProductLines()) {
                if (pl.getProductLine().equalsIgnoreCase(productLine)) {
                    System.err.println("Product line name already exists, try another one. (0 to cancel)");
                    isUnique = false;
                }
            }
        } while (lacksRequirementsUserName(productLine) || (!isUnique));
        return productLine;
    }

    //

    public String setTextDescription() {
        String textDescription;
        boolean validDescription;
        System.out.println("Insert a description for the product line: \n- Text can't be larger than 4000 characters. \n- 0 to cancel.");
         do {
            textDescription = scanner.nextLine();
            validDescription = true;
            if (textDescription.equals("0")) {
                System.out.println("~ ACTION CANCELLED ~");
                break;
            }
            if ((textDescription.length() > 4000)) {
                System.err.println("Description doesn't meet the requirements, try again. (0 to cancel)");
                validDescription = false;
            }
        } while(!validDescription);
        return textDescription;
    }

    //

    public String setHtmlDescription() {
        String htmlDescription;
        boolean validDescription;
        System.out.println("Insert the html description for the product line: \n- Text can't be larger than 16.777.215 characters (MEDIUMTEXT). \n- 0 to cancel.");
        do {
            htmlDescription = scanner.nextLine();
            validDescription = true;
            if (htmlDescription.equals("0")) {
                System.out.println("~ ACTION CANCELLED ~");
                break;
            }
            if ((htmlDescription.length() > 16777215)) {
                System.err.println("Description doesn't meet the requirements, try again. (0 to cancel)");
                validDescription = false;
            }
        } while(!validDescription);
        return htmlDescription;
    }
}
