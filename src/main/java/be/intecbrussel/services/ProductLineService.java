package be.intecbrussel.services;

import be.intecbrussel.data.ProductLineDAO;
import be.intecbrussel.entities.ProductLine;

import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Blob;
import java.sql.SQLException;
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
        if (!Objects.requireNonNull(productLineDAO.getAllProductLines()).isEmpty()) {
            productLineDAO.getAllProductLines().forEach(System.out::println);
        }

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
        if (textDescription != null && textDescription.equals("0"))
            return;
        String htmlDescription = setHtmlDescription();
        if (htmlDescription != null && htmlDescription.equals("0"))
            return;
        Blob image = setImage();
        try {
            if (image != null && image.length() == 1)
                return;
        } catch (SQLException e) {
           e.printStackTrace();
        }
        productLineDAO.addProductLine(new ProductLine(productLine, textDescription, htmlDescription,image));
        System.out.println("~ PRODUCT LINE CREATED ~");
    }

    public void updateProductLine() {
        if (!Objects.requireNonNull(productLineDAO.getAllProductLines()).isEmpty()) {
            System.out.println("Insert the name of the product line that you want to edit: \n- X to show all product lines. \n- 0 to cancel.");
            while (true) {
                String input = scanner.nextLine();
                ProductLine productLine = productLineDAO.getProductLineByName(input);
                if (input.equals("0")) {
                    System.out.println("~ ACTION CANCELLED ~");
                    break;
                } else if (input.equalsIgnoreCase("x")) {
                    showAllProductLines();
                    System.out.println();
                    System.out.println("Insert the name of the product line that you want to edit: \n- X to show all product lines. \n- 0 to cancel.");
                } else if (productLine != null) {
                    System.out.println("What do you want to edit? \n1. Change the name of the product line. \n2. Change the description of the product line. \n3. Change the html description. \n0. Cancel");
                    while (true){
                        try {
                            int choice = scanner.nextInt();
                            if (choice == 1) {
                                scanner.nextLine();
                                productLineDAO.deleteProductLine(productLine);
                                String pl = setProductLine();
                                if(pl != null && pl.equals("0"))
                                    return;
                                productLine.setProductLine(pl);
                                productLineDAO.updateProductLine(productLine);
                                System.out.println("~ NAME UPDATED ~");
                                return;
                            } else if (choice == 2) {
                                scanner.nextLine();
                                String td = setTextDescription();
                                if(td != null && td.equals("0"))
                                    return;
                                productLine.setTextDescription(td);
                                productLineDAO.updateProductLine(productLine);
                                System.out.println("~ TEXT DESCRIPTION UPDATED ~");
                                return;
                            } else if (choice == 3) {
                                scanner.nextLine();
                                String hd = setHtmlDescription();
                                if(hd != null && hd.equals("0"))
                                    return;
                                productLine.setHtmlDescription(hd);
                                productLineDAO.updateProductLine(productLine);
                                System.out.println("~ HTML DESCRIPTION UPDATED ~");
                                return;
                            } else if (choice == 4) {
                                scanner.nextLine();
                                Blob img = setImage();
                                try {
                                    if (img != null && img.length() == 1)
                                        return;
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                productLine.setImage(img);
                                productLineDAO.updateProductLine(productLine);
                                System.out.println("~ IMAGE UPDATED ~");
                                return;
                            } else if (choice == 0) {
                                scanner.nextLine();
                                System.out.println("~ ACTION CANCELLED ~");
                                return;
                            } else
                                System.err.println("Invalid choice, try again. (0 to cancel)");
                        } catch (InputMismatchException e) {
                            System.err.println("Invalid choice, try again. (0 to cancel)");
                        }
                        scanner.nextLine();
                    }
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

    public boolean lacksRequirementsName(String productLine) {
        boolean lacksReq = true;
        if(productLine.length() > 1 && productLine.length() < 51) {
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
        System.out.println("Insert the name that you want to use for your product line: \n- Between 2 and 50 characters. \n- Must have at least 2 alphabetic characters. \n- Can't contain special characters. (except '-') \n- 0 to cancel. ");
        do {
            productLine = scanner.nextLine();
            isUnique = true;
            if (productLine.equals("0")) {
                System.out.println("~ ACTION CANCELLED ~");
                break;
            }
            if (lacksRequirementsName(productLine))
                System.err.println("Product line name doesn't meet the requirements, try again. (0 to cancel)");
            for (ProductLine pl : productLineDAO.getAllProductLines()) {
                if (pl.getProductLine().equalsIgnoreCase(productLine)) {
                    System.err.println("Product line name already exists, try another one. (0 to cancel)");
                    isUnique = false;
                }
            }
        } while (lacksRequirementsName(productLine) || (!isUnique));
        return productLine;
    }

    public String setTextDescription() {
        String textDescription;
        boolean validDescription;
        System.out.println("Insert a description for the product line: \n- Text can't be larger than 4000 characters. \n- Continue to leave empty. \n- 0 to cancel.");
         do {
            textDescription = scanner.nextLine();
            validDescription = true;
            if (textDescription.equals("0")) {
                System.out.println("~ ACTION CANCELLED ~");
                break;
            }
            if (textDescription.equals("")) {
                System.out.println("Are u sure that u want to leave text description empty? Yes/No");
                if(sharedService.yesNo()) {
                    return null;
                } else  {
                    validDescription = false;
                    System.out.println("Insert a description for the product line: \n- Text can't be larger than 4000 characters. \n- Continue to leave empty. \n- 0 to cancel.");
                }
            }
            if ((textDescription.length() > 4000)) {
                System.err.println("Description doesn't meet the requirements, try again. (0 to cancel)");
                validDescription = false;
            }
        } while(!validDescription);
        return textDescription;
    }

    public String setHtmlDescription() {
        String htmlDescription;
        boolean validDescription;
        System.out.println("Insert the html description for the product line: \n- Text can't be larger than 16.777.215 characters (MEDIUMTEXT). \n- Continue to leave empty. \n- 0 to cancel.");
        do {
            htmlDescription = scanner.nextLine();
            validDescription = true;
            if (htmlDescription.equals("0")) {
                System.out.println("~ ACTION CANCELLED ~");
                break;
            }
            if (htmlDescription.equals("")) {
                System.out.println("Are u sure that u want to leave html description empty? Yes/No");
                if(sharedService.yesNo()) {
                    return null;
                } else  {
                    validDescription = false;
                    System.out.println("Insert the html description for the product line: \n- Text can't be larger than 16.777.215 characters (MEDIUMTEXT). \n- Continue to leave empty. \n- 0 to cancel.");
                }
            }
            if ((htmlDescription.length() > 16777215)) {
                System.err.println("Description doesn't meet the requirements, try again. (0 to cancel)");
                validDescription = false;
            }
        } while(!validDescription);
        return htmlDescription;
    }

    public Blob setImage() {
        byte[] byteArray = new byte[1];
        boolean validBlob;
        String input;
        String extension = null;
        Blob blob = null;
        System.out.println("Insert the location of the image for the product line: \n- Must be a file in JPG/PNG format. \n- File can't be larger than 16777215 bytes (MEDIUMBLOB). \n- Continue to leave empty. \n- 0 to cancel.");
        do {
            input = scanner.nextLine();
            validBlob = true;
            if (input.equals("0")) {
                System.out.println("~ ACTION CANCELLED ~");
                try {
                    blob = new SerialBlob(byteArray);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            } else if (input.equals("")) {
                System.out.println("Are u sure that u don't want to insert an image? Yes/No");
                if(sharedService.yesNo()) {
                    return null;
                } else  {
                    validBlob = false;
                    System.out.println("Insert the location of the image for the product line: \n- Must be a file in JPG/PNG format. \n- File can't be larger than 16777215 bytes (MEDIUMBLOB). \n- Continue to leave empty. \n- 0 to cancel.");
                }
            } else {
                try {
                    Scanner pathScanner = new Scanner(new BufferedReader(new FileReader(input)));
                    while (pathScanner.hasNext()) {
                        int i = input.lastIndexOf('.');
                        if (i > 0) {
                            extension = input.substring(i+1);
                        }
                        assert extension != null;
                        if (!extension.equalsIgnoreCase("jpg") && !extension.equalsIgnoreCase("png")) {
                            validBlob = false;
                            System.err.println("File is not in the right format, try another. (0 to cancel)");
                            break;
                        }
                        byteArray = pathScanner.nextLine().getBytes();
                        if (byteArray.length > 16777215) {
                            validBlob = false;
                            System.err.println("File is to large, try another. (0 to cancel)");
                            break;
                        }
                        blob = new SerialBlob(byteArray);
                    }
                } catch (FileNotFoundException | SQLException e) {
                    validBlob = false;
                    System.err.println("File not found, try again. (0 to cancel)");
                }
            }
        } while(!validBlob);
        return blob;
    }
}
