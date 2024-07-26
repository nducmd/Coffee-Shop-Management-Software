package Model;

public class People {

    private String lastName;
    private String firstName;
    private String address;
    private String phoneNumber;
    private String birthdate;
    private String gender;

    public People(String lastName, String firstName, String gender, String birthdate, String address, String phoneNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public People() {
        super();
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public static String standardName(String name) {
        name = name.trim().toLowerCase();
        String[] path = name.split("\\s+");
        String sName = "";
        for (int i = 0; i < path.length; i++) {
            String tmp = String.valueOf(path[i].charAt(0)).toUpperCase() + path[i].substring(1);
            sName += tmp + " ";
        }
        return sName.trim();
    }
}
