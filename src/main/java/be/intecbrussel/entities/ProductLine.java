package be.intecbrussel.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Blob;

@Entity
@Table(name = "productlines")
public class ProductLine {

    @Id
    private String productLine;
    private String textDescription;
    private String htmlDescription;
    private Blob image;

    public ProductLine() {
    }

    public ProductLine(String productLine, String textDescription, String htmlDescription, Blob image) {
        this.productLine = productLine;
        this.textDescription = textDescription;
        this.htmlDescription = htmlDescription;
        this.image = image;
    }

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public String getHtmlDescription() {
        return htmlDescription;
    }

    public void setHtmlDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    @Override
    public String toString() {
        String outputString = "Product line     : " + this.productLine;
        if (this.textDescription != null)
            outputString += "\nDescription      : " + this.textDescription;
        else outputString += "\nDescription      : NO DESCRIPTION FOUND";
        if (this.htmlDescription != null)
            outputString += "\nHTML description : " + this.htmlDescription;
        else outputString += "\nHTML description : NO DESCRIPTION FOUND";
        if (this.image != null) {
            outputString += "\nImage blob code  : " + this.image + "\n";
        }
        else outputString += "\nImage            : NO IMAGE FOUND \n";
        return outputString;
    }
}
