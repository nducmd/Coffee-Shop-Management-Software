/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nducmd
 */
public class ExportExcel {

    public void exportCustomerList(TableView<Customer> customersTable) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Customer Data");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());

            // Đặt tên tệp Excel bao gồm "Danh sách khách hàng" và ngày hiện tại
            // Lấy đường dẫn thư mục gốc của dự án
            String projectDirectory = System.getProperty("user.dir");

            // Tạo đường dẫn tới thư mục Export trong thư mục dự án
            String exportDirectoryPath = projectDirectory + File.separator + "Export";

            // Tạo thư mục Export nếu nó chưa tồn tại
            File exportDirectory = new File(exportDirectoryPath);
            if (!exportDirectory.exists()) {
                exportDirectory.mkdirs();
            }

            // Tạo đường dẫn đầy đủ của tệp Excel
            String excelFileName = exportDirectoryPath + File.separator + "Danh sách khách hàng " + currentDate + ".xlsx";

            // Xuất tiêu đề cột
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < customersTable.getColumns().size() - 1; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(customersTable.getColumns().get(i).getText());
            }

            // Xuất dữ liệu từ TableView
            ObservableList<Customer> data = customersTable.getItems();
            for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                
                row.createCell(0).setCellValue(data.get(rowIndex).getCustomerId());
                row.createCell(1).setCellValue(data.get(rowIndex).getLastName());
                row.createCell(2).setCellValue(data.get(rowIndex).getFirstName());
                row.createCell(3).setCellValue(data.get(rowIndex).getGender());
                row.createCell(4).setCellValue(data.get(rowIndex).getBirthdate());
                row.createCell(5).setCellValue(data.get(rowIndex).getPhoneNumber());
                row.createCell(6).setCellValue(data.get(rowIndex).getAddress());
                row.createCell(7).setCellValue(data.get(rowIndex).getCustomerPoint());
                row.createCell(8).setCellValue(data.get(rowIndex).getCustomerTier());
            }
            // Lưu Excel
            FileOutputStream fileOut = new FileOutputStream(excelFileName);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            // Thông báo xuất thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Dữ liệu đã được xuất ra tệp Excel: " + excelFileName);
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void exportEmployeeList(TableView<Employee> employeeTable) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Employee Data");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());

            // Đặt tên tệp Excel bao gồm "Danh sách nhân viên" và ngày hiện tại
            // Lấy đường dẫn thư mục gốc của dự án
            String projectDirectory = System.getProperty("user.dir");

            // Tạo đường dẫn tới thư mục Export trong thư mục dự án
            String exportDirectoryPath = projectDirectory + File.separator + "Export";

            // Tạo thư mục Export nếu nó chưa tồn tại
            File exportDirectory = new File(exportDirectoryPath);
            if (!exportDirectory.exists()) {
                exportDirectory.mkdirs();
            }

            // Tạo đường dẫn đầy đủ của tệp Excel
            String excelFileName = exportDirectoryPath + File.separator + "Danh sách nhân viên " + currentDate + ".xlsx";

            // Xuất tiêu đề cột
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < employeeTable.getColumns().size() - 1; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(employeeTable.getColumns().get(i).getText());
            }

            // Xuất dữ liệu từ TableView
            ObservableList<Employee> data = employeeTable.getItems();
            for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                
                row.createCell(0).setCellValue(data.get(rowIndex).getEmployeeId());
                row.createCell(1).setCellValue(data.get(rowIndex).getLastName());
                row.createCell(2).setCellValue(data.get(rowIndex).getFirstName());
                row.createCell(3).setCellValue(data.get(rowIndex).getGender());
                row.createCell(4).setCellValue(data.get(rowIndex).getBirthdate());
                row.createCell(5).setCellValue(data.get(rowIndex).getPhoneNumber());
                row.createCell(6).setCellValue(data.get(rowIndex).getAddress());
                row.createCell(7).setCellValue(data.get(rowIndex).getPosition());
                row.createCell(8).setCellValue(data.get(rowIndex).getHoursWorked());
                row.createCell(9).setCellValue(data.get(rowIndex).getHoursWorked() * 25);
            }
            // Lưu Excel
            FileOutputStream fileOut = new FileOutputStream(excelFileName);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            // Thông báo xuất thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Dữ liệu đã được xuất ra tệp Excel: " + excelFileName);
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void exportHistoryList(TableView<PurchaseHistory> employeeTable, String startDate, String endDate) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Purchase History Data");

            
            // Lấy đường dẫn thư mục gốc của dự án
            String projectDirectory = System.getProperty("user.dir");

            // Tạo đường dẫn tới thư mục Export trong thư mục dự án
            String exportDirectoryPath = projectDirectory + File.separator + "Export";

            // Tạo thư mục Export nếu nó chưa tồn tại
            File exportDirectory = new File(exportDirectoryPath);
            if (!exportDirectory.exists()) {
                exportDirectory.mkdirs();
            }

            // Tạo đường dẫn đầy đủ của tệp Excel
            String excelFileName = exportDirectoryPath + File.separator + "Lịch sử bán hàng từ " + startDate  + " đến " + endDate + ".xlsx";

            // Xuất tiêu đề cột
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < employeeTable.getColumns().size() - 1; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(employeeTable.getColumns().get(i).getText());
            }

            // Xuất dữ liệu từ TableView
            ObservableList<PurchaseHistory> data = employeeTable.getItems();
            for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                
                row.createCell(0).setCellValue(data.get(rowIndex).getPurchaseHistoryId());
                row.createCell(1).setCellValue(data.get(rowIndex).getCustomerName());
                row.createCell(2).setCellValue(data.get(rowIndex).getCustomerPhone());
                row.createCell(3).setCellValue(data.get(rowIndex).getTable());
                row.createCell(4).setCellValue(data.get(rowIndex).getTotalMoney());
                row.createCell(5).setCellValue(data.get(rowIndex).getPurchaseDate());
                row.createCell(6).setCellValue(data.get(rowIndex).getEmployeeName());
            }
            // Lưu Excel
            FileOutputStream fileOut = new FileOutputStream(excelFileName);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            // Thông báo xuất thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Dữ liệu đã được xuất ra tệp Excel: " + excelFileName);
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
