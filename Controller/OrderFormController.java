/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.CardProductController;
import DAO.CustomerDAO;
import DAO.EmployeeDAO;
import DAO.OrderDAO;
import DAO.SalesHistoryDAO;
import Model.SalesHistory;
import Model.Session;
import Model.OrderData;
import Model.Product;
import Model.Customer;
import Model.MySence;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author nducmd
 */
public class OrderFormController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button exchangeViewButton;

    @FXML
    private Button qrButton;

    @FXML
    private TextField filterProduct;

    @FXML
    private Label currentDateLable;

    @FXML
    private Button addCusomerButton;

    @FXML
    private TextField selectedTableField;

    @FXML
    private TableColumn<OrderData, Button> deleteColumn;

    @FXML
    private TextField discountField;

    @FXML
    private Label employeeName;

    @FXML
    private Button findCusomerButton;

    @FXML
    private TextField findCustomerField;

    @FXML
    private Label findCustomerResult;

    @FXML
    private Button logoutButton;

    @FXML
    private GridPane menuGridPane;

    @FXML
    private TableColumn<OrderData, String> nameColumn;

    @FXML
    private TextField nameCustomerField;

    @FXML
    private TableView<OrderData> orderTable;

    @FXML
    private Label saveResultLable;

    @FXML
    private Button payButton;

    @FXML
    private TextField phoneNumberCustomerField;

    @FXML
    private TextField pointCustomerField;

    @FXML
    private TableColumn<OrderData, Integer> priceColumn;

    @FXML
    private TableColumn<OrderData, Integer> quantityColumn;

    @FXML
    private TextField receivedMoneyField;

    @FXML
    private TextField returnMoneyField;

    @FXML
    private TextField temporarilyMoneyField;

    @FXML
    private TextField tierCustomerField;

    @FXML
    private TextField totalMoneyField;

    @FXML
    private TableColumn<OrderData, String> unitColumn;

    @FXML
    private TableColumn<OrderData, Double> valueColumn;

    @FXML
    private TextField vatField;

    @FXML
    private Button showEmployeeInfoButton;

    @FXML
    private Button mergeTableButton;

    @FXML
    private TextField tableField;

    private Alert alert;
    private Connection connection;

    private ObservableList<Product> cardList = FXCollections.observableArrayList();
    public static ObservableList<OrderData> orderList = FXCollections.observableArrayList();
    public static ArrayList<ObservableList<OrderData>> orderListArray = new ArrayList<>();
    public static boolean isPaid = false;
    private DoubleProperty temporarilyMoney = new SimpleDoubleProperty(0);
    private IntegerProperty totalMoney = new SimpleIntegerProperty(0);
    private IntegerProperty returnMoney = new SimpleIntegerProperty(0);
    private IntegerProperty selectedTable = new SimpleIntegerProperty(0);

    public void createQrcode() throws IOException, SQLException {
        isPaid = false;
        SalesHistoryDAO shd = new SalesHistoryDAO();
        int idcode = shd.getLastestSalesHistoryId() + 1;
        String link = "https://img.vietqr.io/image/TCB-nducmd-compact2.png?amount=" + totalMoneyField.getText() + "&addInfo=" + "2.11%20coffee%20HD%20" + idcode;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/QRcodeForm.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = new Scene(root);
            // Lấy controller
            QRcodeFormController controller = loader.getController();
            controller.setLink(link);
            // Hiển thị
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Thanh Toán QR");
            stage.setResizable(false);
            stage.showAndWait();
            if (isPaid) {
                receivedMoneyField.setText(totalMoneyField.getText());
                updateBilling();
                saveHistoryAndReceipt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int getTable() {
        if (tableField.getText().equals("")) {
            return 0;
        }
        int tmp = 0;
        try {
            tmp = Integer.parseInt(tableField.getText());
            if (tmp >= Session.numberOfTable) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Bàn không tồn tại.");
                alert.showAndWait();
                tableField.setText(Integer.toString(selectedTable.get()));
                return selectedTable.get();
            }
        } catch (NumberFormatException ex) {
            // Xử lý ngoại lệ nếu người dùng nhập không phải số
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập số hợp lệ.");
            alert.showAndWait();
            tableField.setText(Integer.toString(selectedTable.get()));
            return selectedTable.get();
        }
        return tmp;
    }

    public void selectTable() {
        orderListArray.get(selectedTable.get()).clear();
        orderListArray.get(selectedTable.get()).addAll(orderList);
        selectedTable.set(getTable());
        orderList.clear();
        orderList.addAll(orderListArray.get(selectedTable.get()));
        orderTable.setItems(orderList);
        orderTable.refresh();
    }

    public void mergeTable() {
        orderListArray.get(selectedTable.get()).clear();
        orderListArray.get(selectedTable.get()).addAll(orderList);

        Stage primaryStage = new Stage();
        primaryStage.setTitle("Ghép bàn");

        Label label = new Label("Ghép bàn thứ 1 vào bàn thứ 2");

        TextField number1Input = new TextField();
        number1Input.setPrefWidth(100);
        number1Input.setPromptText("Nhập bàn 1");

        TextField number2Input = new TextField();
        number2Input.setPrefWidth(100);
        number2Input.setPromptText("Nhập bàn 2");

        Label arrowLabel = new Label("-->");

        // Tạo một HBox
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().addAll(number1Input, arrowLabel, number2Input);

        Button submitButton = new Button("Ghép");
        submitButton.setOnAction(e -> {

            String input1 = number1Input.getText();
            String input2 = number2Input.getText();

            try {
                int number1;
                if (input1.equals("")) {
                    number1 = 0;
                } else {
                    number1 = Integer.parseInt(input1);
                }
                int number2 = Integer.parseInt(input2);
                if (number1 >= Session.numberOfTable || number2 >= Session.numberOfTable) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Bàn không tồn tại.");
                    alert.showAndWait();
                } else {
                    if (number1 != number2) {
                        orderList.clear();
                        orderList.addAll(orderListArray.get(number2));
                        CardProductController cpc = new CardProductController();
                        for (OrderData orderData : orderListArray.get(number1)) {
                            cpc.addProductToOrderList(orderData);
                        }
                        orderListArray.get(number1).clear();
                        orderListArray.get(number2).clear();
                        orderListArray.get(number2).addAll(orderList);
                    }
                    tableField.setText(input2);
                    selectedTable.set(number2);
                    selectTable();
                    primaryStage.close();
                }

            } catch (NumberFormatException ex) {
                // Xử lý ngoại lệ nếu người dùng nhập không phải số
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập số hợp lệ.");
                alert.showAndWait();
            }
        });

        // Tạo một VBox
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(label, inputBox, submitButton);

        // Tạo một Scene với VBox
        Scene scene = new Scene(vbox, 400, 150);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showEmployeeInfo() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/EmployeeInfoForm.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setTitle("Thông tin nhân viên");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void initOrderTable() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(column -> {
            return new TableCell<OrderData, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox container = new HBox(5);
                        Button increaseButton = new Button("+");
                        Button decreaseButton = new Button("-");
                        Label quantityLabel = new Label(item.toString());
                        HBox.setMargin(decreaseButton, new Insets(0, 5, 0, 0));
                        HBox.setMargin(quantityLabel, new Insets(0, 5, 0, 0)); 
                        HBox.setMargin(increaseButton, new Insets(0, 0, 0, 5)); 
                        container.getChildren().addAll(decreaseButton, quantityLabel, increaseButton);
                        increaseButton.setOnAction(event -> {
                            int rowIndex = getIndex();
                            if (rowIndex >= 0) {
                                OrderData orderData = orderList.get(rowIndex);
                                int currentQuantity = orderData.getQuantity();
                                currentQuantity++;
                                orderData.setQuantity(currentQuantity);
                                quantityLabel.setText(String.valueOf(currentQuantity));
                                orderTable.refresh();
                                updateBilling();
                            }
                        });

                        decreaseButton.setOnAction(event -> {
                            int rowIndex = getIndex();
                            if (rowIndex >= 0) {
                                OrderData orderData = orderList.get(rowIndex);
                                int currentQuantity = orderData.getQuantity();
                                if (currentQuantity > 0) {
                                    currentQuantity--;
                                    orderData.setQuantity(currentQuantity);
                                    quantityLabel.setText(String.valueOf(currentQuantity));
                                    updateBilling();
                                    if (currentQuantity == 0) {
                                        orderList.remove(orderData);
                                    }
                                }
                                orderTable.refresh();
                            }
                        });

                        setGraphic(container);
                    }
                }
            };
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        valueColumn.setCellValueFactory(param -> {
            OrderData orderData = param.getValue();
            double value = orderData.getQuantity() * orderData.getPrice();
            return new SimpleDoubleProperty(value).asObject();
        });
        deleteColumn.setCellValueFactory(param -> {
            OrderData orderData = param.getValue();
            Button deleteButton = new Button("Xoá");
            deleteButton.setOnAction(event -> orderList.remove(orderData));
            return new SimpleObjectProperty<>(deleteButton);
        });
        orderList.addListener((ListChangeListener<OrderData>) change -> {
            while (change.next()) {
                orderTable.refresh();
                updateBilling();
            }
        });
        orderTable.setItems(orderList);
    }

    // Thanh toán hoá đơn
    public void saveHistoryAndReceipt() throws SQLException {
        if (returnMoney.get() >= 0) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có muốn thanh toán không?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                CustomerDAO cd = new CustomerDAO();
                int id = cd.getCustomerIdByPhoneNumber(phoneNumberCustomerField.getText());
                SalesHistory sh = new SalesHistory( temporarilyMoneyField.getText(), discountField.getText(), vatField.getText(),
                        totalMoneyField.getText(), receivedMoneyField.getText(), returnMoneyField.getText(),
                        selectedTable.get(), id);
                SalesHistoryDAO shd = new SalesHistoryDAO();
                if (shd.add(sh)) {
                    // luu thong tin hoa don vao csdl
                    OrderDAO od = new OrderDAO();
                    od.saveInvoice();
                    if (phoneNumberCustomerField.getText().length() > 1) {
                        // cap nhat diem khach khang trong csdl
                        cd.updateCustomerPoint(phoneNumberCustomerField.getText(), Integer.parseInt(pointCustomerField.getText()) + (int) (totalMoney.get() / 100));
                    }
                    resetBilling();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setContentText("Lưu hoá đơn không thành công!");
                    alert.showAndWait();
                }
            }

        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Số tiền không đủ!");
            alert.showAndWait();
        }
    }

    private void initDisplayBillField() {
        temporarilyMoneyField.textProperty().bind(Bindings.concat(temporarilyMoney));
        totalMoneyField.textProperty().bind(Bindings.concat(totalMoney));
        returnMoneyField.textProperty().bind(Bindings.concat(returnMoney));
        selectedTableField.textProperty().bind(Bindings.concat(selectedTable));
    }

    private void initCustomer() {
        findCustomerField.setText("");
        findCustomerResult.setText("");
        nameCustomerField.setText("Khách lẻ");
        phoneNumberCustomerField.setText("");
        tierCustomerField.setText("Không");
        pointCustomerField.setText("0");
    }

    private void resetBilling() {
        setDiscount("");
        initCustomer();
        //temporarilyMoney.set(0);
        //totalMoney.set(0);
        //returnMoney.set(0);
        receivedMoneyField.setText("0");
        orderList.clear();
        updateBilling();

    }

    private void changeColorReturnMoneyField(int value) {
        if (value < 0) {
            returnMoneyField.setStyle("-fx-text-fill: red;");
        } else {
            returnMoneyField.setStyle("-fx-text-fill: green;");
        }
    }

    public void updateBilling() {
        double sumValue = orderList.stream()
                .mapToDouble(product -> product.getQuantity() * product.getPrice())
                .sum();
        temporarilyMoney.set(sumValue);
        double afterDiscount = 100 - Double.parseDouble(discountField.getText());
        sumValue = sumValue / 100 * afterDiscount;
        sumValue = sumValue / 100 * 110;
        totalMoney.set((int) Math.ceil(sumValue));
        int received = Integer.parseInt(receivedMoneyField.getText());
        returnMoney.set(received - totalMoney.get());
        changeColorReturnMoneyField(returnMoney.get());
    }

    public void filterProduct() throws SQLException, IOException {
        menuGridPane.getChildren().clear();
        menuDisplay(filterProduct.getText());
    }

    private void menuDisplay(String name) throws SQLException, IOException {
        cardList.clear();
        OrderDAO od = new OrderDAO();
        cardList.addAll(od.getMenuData(name));

        int row = 0;
        int col = 0;
        menuGridPane.getRowConstraints().clear();
        menuGridPane.getColumnConstraints().clear();

        for (int i = 0; i < cardList.size(); i++) {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/View/CardProduct.fxml"));
            AnchorPane pane = load.load();
            CardProductController cardC = load.getController();
            cardC.setData(cardList.get(i));
            
            if (col == 4) {
                col = 0;
                row = row + 1;
            }
            menuGridPane.add(pane, col++, row);

        }
    }

    public void logout() {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có muốn đăng xuất không?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                Session.setEndTime();
                EmployeeDAO ed = new EmployeeDAO();
                ed.updateHoursWorkedEmployee();
                logoutButton.getScene().getWindow().hide();

                MySence ms = new MySence("/View/LoginForm.fxml", "Quản lí cửa hàng", false, false);
                ms.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findCustomer() throws SQLException {
        setDiscount("");
        updateBilling();
        findCustomerResult.setText("");
        nameCustomerField.setText("Khách lẻ");
        phoneNumberCustomerField.setText("");
        tierCustomerField.setText("Không");
        pointCustomerField.setText("0");
        String phone = findCustomerField.getText();
        if (phone.equals("")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Vui lòng nhập số điện thoại!");
            alert.showAndWait();
        } else {
            CustomerDAO cd = new CustomerDAO();
            Customer c = cd.getCustomerByPhoneNumber(phone);
            if (c != null) {
                nameCustomerField.setText(c.getLastName() + " " + c.getFirstName());
                phoneNumberCustomerField.setText(phone);
                pointCustomerField.setText(Integer.toString(c.getCustomerPoint()));
                tierCustomerField.setText(c.getCustomerTier());
                setDiscount(tierCustomerField.getText());
                updateBilling();
                return;
            }
            findCustomerResult.setText("Không tìm thấy khách hàng");
        }

    }

    private void setDiscount(String tier) {
        tier = tier.trim().toLowerCase();
        if (tier.equals("bạc")) {
            discountField.setText("1");
        } else if (tier.equals("vàng")) {
            discountField.setText("2");
        } else if (tier.equals("bạch kim")) {
            discountField.setText("3");
        } else if (tier.equals("kim cương")) {
            discountField.setText("4");
        } else {
            discountField.setText("0");
        }
    }

    public void addCustomer() throws IOException {
        MySence ms = new MySence("/View/RegisterCustomerForm.fxml", "Đăng ký", false, false);
        ms.showAndWait();
    }

    private void displayName() {
        employeeName.setText(Session.currEmployee.getLastName() + " " + Session.currEmployee.getFirstName());
    }

    private void displayCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd-MM-yyyy", new Locale("vi", "VN"));
        String currentTime = dateFormat.format(new Date());

        currentDateLable.setText(currentTime);
    }

    public void exchangeView() throws IOException {
        MySence ms = new MySence("/View/ManagementForm.fxml", "Quản lí cửa hàng", false, true);
        ms.showAndNotClose();
        exchangeViewButton.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Session.currEmployee.getPosition().toLowerCase().equals("nhân viên")) {
            exchangeViewButton.setVisible(false);
        }
        orderList.clear();
        orderListArray.clear();
        for (int i = 0; i <= Session.numberOfTable; i++) {
            orderListArray.add(FXCollections.observableArrayList());
        }
        try {
            menuDisplay("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        displayCurrentDate();
        initDisplayBillField();
        initOrderTable();
        displayName();
    }

}
