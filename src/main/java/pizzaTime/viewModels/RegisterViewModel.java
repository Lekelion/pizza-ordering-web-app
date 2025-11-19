package pizzaTime.viewModels;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import pizzaTime.entities.Customer;

public class RegisterViewModel {

    @Size(min=4, max=24)
    String username;

    @Size(min=4, max=24)
    String password;

    @NotEmpty
    String password2;

    @Email
    String email;

    String address1, address2, city, province;
    String postalCode;

    public RegisterViewModel() {
    }

    public Customer getCustomer() {
        Customer c = new Customer(username, password, email);
        c.setAddress1(address1);
        c.setAddress2(address2);
        c.setCity(city);
        c.setProvince(province);
        c.setPostalCode(postalCode);
        return c;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getPassword2() {return password2;}
    public void setPassword2(String password2) {this.password2 = password2;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

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
        return "{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
