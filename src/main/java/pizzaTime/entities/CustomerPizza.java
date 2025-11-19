package pizzaTime.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.math.BigDecimal;

@Entity
public class CustomerPizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private CustomerOrder order;

    private int quantity = 1;

    @ManyToOne(fetch = FetchType.EAGER)
    private PizzaSize pizzaSize;

    @ManyToOne(fetch = FetchType.EAGER)
    private PizzaCrust pizzaCrust;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal priceEach = BigDecimal.ZERO;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    @Transient
    private int number;
    public int getNumber() {return number;}
    public void setNumber(int number) {this.number = number;}

    public CustomerOrder getOrder() {return order;}
    public void setOrder(CustomerOrder order) {this.order = order;}

    public PizzaSize getPizzaSize() {return pizzaSize;}
    public void setPizzaSize(PizzaSize pizzaSize) {this.pizzaSize = pizzaSize;}

    public PizzaCrust getPizzaCrust() {return pizzaCrust;}
    public void setPizzaCrust(PizzaCrust pizzaCrust) {this.pizzaCrust = pizzaCrust;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public BigDecimal getPriceEach() {return priceEach;}
    public void setPriceEach(BigDecimal priceEach) {this.priceEach = priceEach;}

    public BigDecimal getTotalPrice() {return totalPrice;}
    public void setTotalPrice(BigDecimal totalPrice) {this.totalPrice = totalPrice;}

    public String getDescription() {
        return pizzaSize.getName() +  " with " + pizzaCrust.getName();
    }

    public BigDecimal computePrice() {
        // TODO: Step 3 - compute the total price for this record
        return totalPrice;
    }

    public void updateFrom(CustomerPizza p) {
        this.pizzaSize = p.getPizzaSize();
        this.pizzaCrust = p.getPizzaCrust();
        this.quantity = p.getQuantity();
        computePrice();
    }

    @Override
    public String toString() {
        return "CustomerPizza{" +
                "quantity=" + quantity +
                ", pizzaSize=" + pizzaSize +
                ", pizzaCrust=" + pizzaCrust +
                ", priceEach=" + priceEach +
                ", totalPrice=" + totalPrice +
                '}';
    }
}