package Model;

public class Customer extends People {
    private int customerId;
    private int customerPoint;
    private String customerTier;
    
    public Customer(int id, String lastName, String firstName, String gender, String birthdate, String address, String phoneNumber, int customerPoint, String customerTier) {
        super(lastName, firstName, gender, birthdate, address, phoneNumber);
        this.customerId = id;
        this.customerPoint = customerPoint;
        this.customerTier = customerTier;
    }
    public Customer(String lastName, String firstName, String gender, String birthdate, String address, String phoneNumber) {
        super(lastName, firstName, gender, birthdate, address, phoneNumber);
        this.customerPoint = customerPoint;
        this.customerTier = customerTier;
    }
    
    
    public Customer(int customerId, int customerPoint, String customerTier, 
            String lastName, String firstName, String gender, 
            String birthdate, String address, String phoneNumber) {
        super(lastName, firstName, gender, birthdate, address, phoneNumber);
        this.customerId = customerId;
        this.customerPoint = customerPoint;
        this.customerTier = customerTier;
    }
    
    public Customer() {
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public int getCustomerId() {
        return customerId;
    }

    public int getCustomerPoint() {
        return customerPoint;
    }

    public void setCustomerPoint(int customerPoint) {
        this.customerPoint = customerPoint;
    }

    public String getCustomerTier() {
        return customerTier;
    }

    public void setCustomerTier(String customerTier) {
        this.customerTier = customerTier;
    }

    public static String getTierByPoint(int customerPoint) {
        if (customerPoint >= 60000) {
            return "Kim Cương";
        } else if (customerPoint >= 30000) {
            return "Bạch Kim";
        } else if (customerPoint >= 10000) {
            return "Vàng";
        } else if (customerPoint >= 5000) {
            return "Bạc";
        } else {
            return "Không";
        }
    }

}
