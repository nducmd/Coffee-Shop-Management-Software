/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author nducmd
 */
public class OrderData {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty unit;
    private SimpleIntegerProperty quantity;
    private SimpleIntegerProperty price;
    
    public OrderData(int id, String name, String unit, int quantity, int price) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.unit = new SimpleStringProperty(unit);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleIntegerProperty(price);
    }

    public int getId() {
        return id.get();
    }
    
    public String getName() {
        return name.get();
    }
    
    public String getUnit() {
        return unit.get();
    }
    
    public int getQuantity() {
        return quantity.get();
    }

    public int getPrice() {
        return price.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }
}
