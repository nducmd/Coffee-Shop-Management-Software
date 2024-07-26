/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author nducmd
 */
public class PurchaseHistory {
    private int purchaseHistoryId;
    private String totalMoney;
    private String purchaseDate;
    private String employeeName;
    private String table;
    private String customerName;
    private String customerPhone;
    public PurchaseHistory(int purchaseHistoryId, String totalMoney, String purchaseDate, 
            String employeeName, String table, String customerName, String customerPhone) {
        this.purchaseHistoryId = purchaseHistoryId;
        this.totalMoney = totalMoney;
        this.purchaseDate = convertTime(purchaseDate);
        this.employeeName = employeeName;
        this.table = table;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    
    private String convertTime(String dateTimeString) {
        // Định dạng của chuỗi ngày và giờ
        if (dateTimeString.length() == 22) {
            dateTimeString = dateTimeString + "0";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        // Chuyển chuỗi thành đối tượng LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        // Tách thành ngày và giờ
        String ngay = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String gio = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        
//        System.out.println("Ngày: " + ngay);
//        System.out.println("Giờ: " + gio);
        return ngay + " " + gio;
    }
    public int getPurchaseHistoryId() {
        return purchaseHistoryId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getTable() {
        return table;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    
    
}
