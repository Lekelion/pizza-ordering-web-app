package pizzaTime.viewModels;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pizzaTime.entities.Customer;

public class ProfileViewModel {

    String username;

    @Email
    String email;

    String address1, address2, city, province;

    String postalCode;

    public ProfileViewModel() {
    }

    public ProfileViewModel(Customer c) {
        username = c.getUsername();
        email = c.getEmail();
        address1 = c.getAddress1();
        address2 = c.getAddress2();
        city = c.getCity();
        province = c.getProvince();
        postalCode = c.getPostalCode();
    }

    public void update(Customer c) {
        c.setEmail(email);
        c.setAddress1(address1);
        c.setAddress2(address2);
        c.setCity(city);
        c.setProvince(province);
        c.setPostalCode(postalCode);
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

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
        return "ProfileViewModel{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }

}
