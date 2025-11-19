package pizzaTime.controllers;

import pizzaTime.entities.Customer;
import pizzaTime.services.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@Controller
public class HomeController {

    LoginService loginService;

    public HomeController(LoginService loginService) {
        this.loginService = loginService;
    }

    // this attribute gets added to the Model for every request handler in this class
    @ModelAttribute("user")
    public Customer getUser(HttpSession session) {
        return loginService.getCustomer();
    }

    @GetMapping("/")
    public String home(Model model) {
        dumpModelAttributes(model);
        return "home";
    }

    void dumpModelAttributes(Model model) {
        System.out.println("Model dump:");
        for (Map.Entry<String, Object> entry : model.asMap().entrySet())
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
    }
}
