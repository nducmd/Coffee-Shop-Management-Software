package Model;

import Model.People;

public class Employee extends People{
    private int employeeId;
    private String password;
    private int hoursWorked;
    private String position;

    public Employee(int employeeId, String password, int hoursWorked, 
            String position, String lastName, String firstName, String gender, 
            String birthdate, String address, String phoneNumber) {
        super(lastName, firstName, gender, birthdate, address, phoneNumber);
        this.employeeId = employeeId;
        this.password = password;
        this.hoursWorked = hoursWorked;
        this.position = position;
    }

    
    public Employee(String lastName, String firstName, String gender, String birthdate, String address, String phoneNumber, String password) {
        super(lastName, firstName, gender, birthdate, address, phoneNumber);
        this.password = password;
    }

    public Employee() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getPassword() {
        return password;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
