package pizzaTime.config;

import pizzaTime.services.LoginService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    LoginService loginService;

    public LoginInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object handler) throws Exception {

        System.out.println("Interceptor checking " + request.getRequestURI());
        if (loginService.isLoggedIn())
            return true;

        System.out.println("Intercepted! " + request.getRequestURI());
        response.sendRedirect(request.getContextPath() + "/customer");
        return false;
    }
}
