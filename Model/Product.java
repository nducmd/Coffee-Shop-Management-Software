/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author nducmd
 */
public class Product {
    private int id;
    private String productName;
    private String unit;
    private int price;
    private String image;
    private String status;

    public Product(String productName, String unit, int price, String image, String status) {
        this.productName = productName;
        this.unit = unit;
        this.price = price;
        this.image = image;
        this.status = status;
    }
    
    public Product(int id, String productName, String unit, int price, String image, String status) {
        this.id = id;
        this.productName = productName;
        this.unit = unit;
        this.price = price;
        this.image = image;
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getStatus() {
        return status;
    }
    public void setAvailable(String status) {
        this.status = status;
    }
}
