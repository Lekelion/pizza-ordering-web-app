package pizzaTime;

import org.junit.jupiter.api.Test;
import pizzaTime.entities.CustomerOrder;
import pizzaTime.entities.CustomerPizza;
import pizzaTime.entities.PizzaCrust;
import pizzaTime.entities.PizzaSize;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Step3Test {

    @Test
    void testPrices() {
        CustomerPizza p1 = new CustomerPizza();
        p1.setPizzaSize(new PizzaSize("Large", BigDecimal.valueOf(12)));
        p1.setPizzaCrust(new PizzaCrust("Chicago Style", BigDecimal.valueOf(4)));
        p1.setQuantity(2);
        System.out.println("Pizza1=" + p1);

        CustomerPizza p2 = new CustomerPizza();
        p2.setPizzaSize(new PizzaSize("Medium", BigDecimal.valueOf(8)));
        p2.setPizzaCrust(new PizzaCrust("Chicago Style", BigDecimal.valueOf(4)));
        p2.setQuantity(3);
        System.out.println("Pizza2=" + p2);

        CustomerOrder o1 = new CustomerOrder();
        o1.addPizza(p1);
        o1.addPizza(p2);
        o1.computePrice();
        System.out.println("Order=" + o1);

        assertEquals(32, p1.getTotalPrice().intValue()); // 2*(12+4) = 32
        assertEquals(36, p2.getTotalPrice().intValue()); // 3*(8+4) = 36
        assertEquals(68, o1.getSubTotal().intValue());   // 68 * 1.15 = 78
        assertEquals(78, o1.getGrandTotal().intValue()); // 68 * 1.15 = 78
    }
}
