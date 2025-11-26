package pizzaTime.services;

import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;
import pizzaTime.entities.Customer;
import pizzaTime.entities.CustomerPizza;
import pizzaTime.entities.CustomerRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final String SESSION_USER_ID = "userID";

    PasswordEncoder passwordEncoder;
    CustomerRepo userInfoRepo;
    HttpSession session;

    public LoginService(PasswordEncoder passwordEncoder, CustomerRepo userInfoRepo, HttpSession session) {
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepo = userInfoRepo;
        this.session = session;
    }

    public boolean register(Customer user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userInfoRepo.save(user);
            session.setAttribute(SESSION_USER_ID, user.getId());
            System.out.println("Register Success");
            return true;
        } catch (Exception ex) {
            System.out.println("User Save failed:" + ex.getMessage());
            return false;
        }
    }

    public boolean update(Customer user) {
        try {
            userInfoRepo.save(user);
            System.out.println("Update Success");
            return true;
        } catch (Exception ex) {
            System.out.println("User Save failed:" + ex.getMessage());
            return false;
        }
    }

    public boolean userExists(String username) {
        Customer dbUser = userInfoRepo.findByUsername(username);
        if (dbUser != null) System.out.println("Existing User:" + dbUser);
        return dbUser != null;
    }

    public boolean authenticate(Customer loginUser) {
        if (loginUser == null)
            return false;
        Customer dbUser = userInfoRepo.findByUsername(loginUser.getUsername());
        if (dbUser == null)
            return false;
        if (!isValidPassword(loginUser.getPassword(), dbUser.getPassword()))
            return false;

        // Success !
        session.setAttribute(SESSION_USER_ID, dbUser.getId());
        System.out.println("Login Success !");
        return true;
    }

    public void logout() {
        System.out.println("Logout");
        session.removeAttribute(SESSION_USER_ID);
    }

    public boolean isLoggedIn() {
        return getUserId() != 0;
    }

    public Customer getCustomer() {
        int id = getUserId();
        if (id <= 0)
            return null;
        return userInfoRepo.findById(id);
    }

    // ****** private methods ******

    private boolean isValidPassword(String plainTextPassword, String hashedPassword) {
        return passwordEncoder.matches(plainTextPassword, hashedPassword);
    }

    static boolean bFirst = true;
    public static final Customer autoLoginUser =
            //TODO: Step 1 - enter your own user name & password here
            new Customer("Leke", "$2a$10$vZrNu/Ngh0FsFX2zhkspf.T09MOaU8nRf3rXoeC2SJ0h6WzXrzz9O");
    private int getUserId() {
        // auto-login user=Chris
        if (bFirst) {
            bFirst = false;
            Customer me = userInfoRepo.findByUsername(autoLoginUser.getUsername());
            if (me != null) {
                System.out.println("auto login");
                session.setAttribute(SESSION_USER_ID, me.getId());
                return me.getId();
            }
        }
        Object obj = session.getAttribute(SESSION_USER_ID);
        if (obj == null)
            return 0;
        return (int) obj;
    }

}
