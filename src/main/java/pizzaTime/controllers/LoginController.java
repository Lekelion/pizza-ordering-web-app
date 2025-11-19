package pizzaTime.controllers;

import pizzaTime.entities.Customer;
import pizzaTime.services.LoginService;
import pizzaTime.viewModels.ProfileViewModel;
import pizzaTime.viewModels.RegisterViewModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("login", new RegisterViewModel());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(
            @ModelAttribute("login") Customer loginUser,
            BindingResult result) {
        System.out.println("Login:" + loginUser);
        if (!loginService.authenticate(loginUser)) {
            System.out.println("  failed");
            result.rejectValue("username", "failed", "Login failed");
            return "login";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        loginService.logout();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerGet(Model model) {
        model.addAttribute("customer", new RegisterViewModel());
        return "customer/register";
    }

    @PostMapping("/register")
    public String registerPost(
            @Valid @ModelAttribute("customer") RegisterViewModel vmCustomer,
            BindingResult result) {

        System.out.println("Register:" + vmCustomer);
        if (result.hasErrors()) {
            System.out.println("  not valid");
            for (ObjectError error : result.getAllErrors())
                System.out.println("  " + error);
            return "customer/register";
        }

        if (!vmCustomer.getPassword().equals(vmCustomer.getPassword2())) {
            System.out.println("  not match");
            result.rejectValue("password", "noMatch", "Passwords do not match");
            return "customer/register";
        }

        if (loginService.userExists(vmCustomer.getUsername())) {
            System.out.println("  taken");
            result.rejectValue("username", "taken", "This Username is already taken");
            return "customer/register";
        }

        if (!loginService.register(vmCustomer.getCustomer())) {
            System.out.println("  dbError");
            result.rejectValue("username", "dbError", "Unable to Save");
            return "customer/register";
        }

        // register success
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String profileGet(Model model) {
        model.addAttribute("customer", new ProfileViewModel(loginService.getCustomer()));
        return "customer/edit";
    }

    @PostMapping("/profile")
    public String profilePost(
            @Valid @ModelAttribute("customer") ProfileViewModel vmCustomer,
            BindingResult result) {

        System.out.println("EditProfile:" + vmCustomer);
        if (result.hasErrors()) {
            System.out.println("  not valid");
            for (ObjectError error : result.getAllErrors())
                System.out.println("  " + error);
            return "customer/edit";
        }

        Customer dbCustomer = loginService.getCustomer();
        vmCustomer.update(dbCustomer);
        if (!loginService.update(dbCustomer)) {
            System.out.println("  dbError");
            result.rejectValue("username", "dbError", "Unable to Save");
            return "customer/edit";
        }

        return "redirect:/";
    }
}