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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SalesHistory {
    
    private String totalMoney, returnMoney;
    private String temporarilyMoney, discount, vat, receivedMoney;
    private String saleDate;
    // Trên lớp thầy dặn không code theo kiểu dùng id để liên kết mà phải dùng object
    // Xin phép thầy nhóm dùng cách dùng id trong bài này, nhóm sẽ thay đổi trong nhũng bài sau
    private int employeeId, table, customerId;
    // Constructor
    public SalesHistory(String temporarilyMoney, String discount, String vat, String totalPrice, 
            String receivedMoney, String returnMoney, int table, int customerId) {
        this.totalMoney = totalPrice;
        this.employeeId = Session.currEmployee.getEmployeeId();
        this.saleDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).toString();
        this.discount = discount;
        this.temporarilyMoney = temporarilyMoney;
        this.vat = vat; 
        this.receivedMoney = String.valueOf(Integer.parseInt(receivedMoney));
        this.returnMoney = returnMoney;
        this.table = table;
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }
    
    public String getReturnMoney() {
        return returnMoney;
    }

    public String getTemporarilyMoney() {
        return temporarilyMoney;
    }

    public String getDiscount() {
        return discount;
    }

    public String getVat() {
        return vat;
    }

    public String getReceivedMoney() {
        return receivedMoney;
    }
    

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }


    public String getTotalMoney() {
        return totalMoney;
    }

    public int getTable() {
        return table;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

}
