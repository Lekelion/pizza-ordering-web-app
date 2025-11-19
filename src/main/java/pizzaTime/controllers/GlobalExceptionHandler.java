package pizzaTime.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model, HttpServletRequest request) {

        if (!request.getRequestURI().contains("favicon")) {
            System.out.println();
            System.out.println("*** EXCEPTION ***");
            System.out.println("URI: " + request.getRequestURI());
            System.out.println("Method: " + request.getMethod());
            System.out.println("Exception: " + ex.getClass().getName());
            System.out.println("Message: " + ex.getMessage());
            if (ex.getCause() != null)
                System.out.println("InnerException: " + ex.getCause().getMessage());
            System.out.println();
        }

        model.addAttribute("URI", request.getRequestURI());
        model.addAttribute("Method", request.getMethod());
        model.addAttribute("Exception", ex.getClass().getName());
        model.addAttribute("ErrorMessage", ex.getMessage());

        return "unhandledError";
    }

}
