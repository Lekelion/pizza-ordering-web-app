package pizzaTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import pizzaTime.entities.*;
import pizzaTime.services.LoginService;

import java.math.BigDecimal;

@SpringBootApplication
public class PizzaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaApplication.class, args);
    }

    @Bean
    @Order(1)
    CommandLineRunner createUser(CustomerRepo customerRepo) {
        return (args) -> {
            if (customerRepo.findByUsername(LoginService.autoLoginUser.getUsername()) == null) {
                System.out.println("Auto-Add, user=" + LoginService.autoLoginUser.getUsername());
                customerRepo.save(LoginService.autoLoginUser);
            }
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner checkPizzaSize(PizzaSizeRepo repo) {
        return (args) -> {
            if (repo.findAll().isEmpty()) {
                System.out.println("Auto-Add PizzaSize");
                repo.save(new PizzaSize("Small", BigDecimal.valueOf(6)));
                repo.save(new PizzaSize("Medium", BigDecimal.valueOf(8)));
                repo.save(new PizzaSize("Large", BigDecimal.valueOf(12)));
                repo.save(new PizzaSize("Extra-Large", BigDecimal.valueOf(16)));
            }
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner checkPizzaCrust(PizzaCrustRepo repo) {
        return (args) -> {
            if (repo.findAll().isEmpty()) {
                System.out.println("Auto-Add PizzaCrust");
                repo.save(new PizzaCrust("Original Crust", BigDecimal.ZERO));
                repo.save(new PizzaCrust("Thin Crust", BigDecimal.ZERO));
                repo.save(new PizzaCrust("Handmade Pan", BigDecimal.valueOf(2)));
                repo.save(new PizzaCrust("Gluten-Free Crust", BigDecimal.valueOf(2)));
                repo.save(new PizzaCrust("Chicago Style", BigDecimal.valueOf(4)));
            }
        };
    }

    @Bean
    @Order(3)
    CommandLineRunner createOrders(
            CustomerRepo customerRepo,
            CustomerOrderRepo orderRepo,
            PizzaSizeRepo sizeRepo,
            PizzaCrustRepo crustRepo) {
        return (args) -> {
            Customer c = customerRepo.findByUsername("Chris");
            if (c == null) return;
            if (!orderRepo.findByCustomerId(c.getId()).isEmpty()) return;

            PizzaBuilder b = new PizzaBuilder(sizeRepo, crustRepo);

            System.out.println("creating new pizza order");
            CustomerOrder o1 = new CustomerOrder();
            o1.setCustomer(c);
            o1.setOrderStatus(CustomerOrder.OrderStatus.DELIVERED);
            o1.setPaymentType(CustomerOrder.PaymentType.CASH);
            o1.addPizza(b.createPizza(1,1,1));
//            o1.computePrice();
            orderRepo.save(o1);

            CustomerOrder o2 = new CustomerOrder();
            o2.setCustomer(c);
            o2.addPizza(b.createPizza(2,2,2));
            o2.addPizza(b.createPizza(4,4,4));
//            o2.computePrice();
            orderRepo.save(o2);
        };
    }

    static class PizzaBuilder {
        PizzaSizeRepo sizeRepo;
        PizzaCrustRepo crustRepo;

        public PizzaBuilder(PizzaSizeRepo sizeRepo, PizzaCrustRepo crustRepo) {
            this.sizeRepo = sizeRepo;
            this.crustRepo = crustRepo;
        }

        public CustomerPizza createPizza(int sizeId, int crustId, int qty) {
            PizzaSize pSize = sizeRepo.findById(sizeId).orElse(null);
            PizzaCrust pCrust = crustRepo.findById(crustId).orElse(null);
            if (pSize == null || pCrust == null) return null;

            CustomerPizza p1 = new CustomerPizza();
            p1.setPizzaSize(pSize);
            p1.setPizzaCrust(pCrust);
            p1.setQuantity(qty);
            return p1;
        }
    }
}