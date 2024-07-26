/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.CustomerDAO;
import DAO.EmployeeDAO;
import DAO.ProductDAO;
import DAO.SalesHistoryDAO;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author nducmd
 */
public class DashboardFormController implements Initializable {
    
    @FXML
    private ComboBox<String> monthFilterBox;
    
    @FXML
    private Text todayRevenueText;

    @FXML
    private Text todayTrafficText;
    
    @FXML
    private Text countProductText;

    @FXML
    private Text countCustomerText;

    @FXML
    private Text countEmployeeText;
    
    private final ObservableList<String> monthList = FXCollections.observableArrayList("Tháng 1", "Tháng 2",
            "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12");
    @FXML
    private LineChart<?, ?> revenueLineChart;
    
    @FXML
    private BarChart<?, ?> customerBarChart;

    public void showRevenueLineChart(int month) {
        revenueLineChart.getData().clear();
        SalesHistoryDAO shd = new SalesHistoryDAO();
        int[] revenue = shd.getRevenueByDay(month);
        revenueLineChart.setTitle("Doanh thu theo ngày trong tháng");
        XYChart.Series series = new XYChart.Series<>();
        series.setName("Doanh thu");
        for (int i = 0; i < revenue.length; i++) {
            series.getData().add(new XYChart.Data<>(Integer.toString(i + 1), revenue[i]));
        }
        revenueLineChart.getData().add(series);
    }
    public void showTrafficBarChart(int month) {
        customerBarChart.getData().clear();
        SalesHistoryDAO shd = new SalesHistoryDAO();
        int[] customer = shd.getTrafficByDay(month);
        customerBarChart.setTitle("Lượt bán theo ngày trong tháng");
        XYChart.Series series = new XYChart.Series<>();
        series.setName("Lượt bán");
        for (int i = 0; i < customer.length; i++) {
            series.getData().add(new XYChart.Data<>(Integer.toString(i + 1), customer[i]));
        }
        customerBarChart.getData().add(series);
    }
    public void filterMonth() {
        String month = monthFilterBox.getValue();
        String[] path = month.split("\\s+");
        showRevenueLineChart(Integer.parseInt(path[1]));
        showTrafficBarChart(Integer.parseInt(path[1]));
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int month = LocalDate.now().getMonth().getValue();
        showRevenueLineChart(month);
        showTrafficBarChart(month);
        monthFilterBox.setItems(monthList);
        monthFilterBox.setValue(monthList.get(month-1));
        countCustomerText.setText(Integer.toString(new CustomerDAO().getNumberOfCustomer()));
        countEmployeeText.setText(Integer.toString(new EmployeeDAO().getNumberOfEmployee()));
        countProductText.setText(Integer.toString(new ProductDAO().getNumberOfProduct()));
        todayTrafficText.setText(Integer.toString(new SalesHistoryDAO().getTrafficToday()));
        todayRevenueText.setText(Integer.toString(new SalesHistoryDAO().getRevenueToday()));
    }

}
