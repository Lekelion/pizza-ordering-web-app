package pizzaTime.entities;

import jakarta.persistence.*;
import pizzaTime.viewModels.ProfileViewModel;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private boolean admin;

    private String address1;
    private String address2;
    private String city;
    private String province;
    private String postalCode;

    public Customer() {}

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Customer(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {return id;}
    public void setId(int  id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public boolean isAdmin() {return admin;}
    public void setAdmin(boolean admin) {this.admin = admin;}

    public String getAddress1() {return address1;}
    public void setAddress1(String address1) {this.address1 = address1;}

    public String getAddress2() {return address2;}
    public void setAddress2(String address2) {this.address2 = address2;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    public String getProvince() {return province;}
    public void setProvince(String province) {this.province = province;}

    public String getPostalCode() {return postalCode;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}