package pizzaTime.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public enum OrderType {UNKNOWN, PICKUP, DELIVERY};
    OrderType orderType = OrderType.PICKUP;

    public enum OrderStatus {UNKNOWN, NEW, SUBMITTED, DELIVERED};
    OrderStatus orderStatus = OrderStatus.NEW;

    @Column(nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    private LocalDateTime pickupTime;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal subTotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal hst = BigDecimal.ZERO;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal tip = BigDecimal.ZERO;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal amountPaid = BigDecimal.ZERO;

    public enum PaymentType {UNKNOWN, DEBIT, CREDIT, CASH};
    PaymentType paymentType;

    @OneToMany(mappedBy = "order",
            fetch=FetchType.EAGER,      // required to get the initial list of Tasks
            cascade=CascadeType.ALL,    // required to update Pizzas when we update User
            orphanRemoval=true)         // required to be able to delete Pizzas
    private List<CustomerPizza> details = new ArrayList<CustomerPizza>();

    // pizza's within an order are addressed by number
    // and in order to put a PizzaNumber in the HTML,
    // we need a PizzaNumber in our CustomerPizza object
    //
    @PostLoad
    public void updatePizzaNumbers() {
        int n = 0;
        for (CustomerPizza pizza : details)
            pizza.setNumber(++n);
    }

    public void addPizza(CustomerPizza pizza) {
        pizza.setOrder(this);
        details.add(pizza);
    }

    public int getPizzaCount() {
        int count = 0;
        for (CustomerPizza pizza : details)
            count += pizza.getQuantity();
        return count;
    }

    public CustomerPizza getPizzaById(int id) {
        for (CustomerPizza pizza : details)
            if (id == pizza.getId())
                return pizza;
        return null;
    }

    public CustomerPizza getPizzaByNumber(int n) {
        // pizza 'numbers' start at 1
        if (n <= 0 || n > details.size())
            return null;
        return details.get(n-1);
    }

    public void computePrice() {
        // TODO: Step 3 - compute the total price for this order
        System.out.println("computePrice");
    }

    public void computePickupTime() {
        // TODO: Step 6 - compute the pickup time
        // now() + 20 minutes per pizza, plus no-tip-delay (20 minutes)
    }

    // getters & setters

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public Customer getCustomer() {return customer;}
    public void setCustomer(Customer customer) {this.customer = customer;}

    public OrderType getOrderType() {return orderType;}
    public void setOrderType(OrderType orderType) {this.orderType = orderType;}

    public OrderStatus getOrderStatus() {return orderStatus;}
    public void setOrderStatus(OrderStatus status) {this.orderStatus = status;}

    public boolean isNew() {return orderStatus == OrderStatus.NEW;}

    public BigDecimal getSubTotal() {return subTotal;}
    public void setSubTotal(BigDecimal subTotal) {this.subTotal = subTotal;}

    public BigDecimal getHst() {return hst;}
    public void setHst(BigDecimal hst) {this.hst = hst;}

    public BigDecimal getGrandTotal() {return grandTotal;}
    public void setGrandTotal(BigDecimal grandTotal) {this.grandTotal = grandTotal;}

    public BigDecimal getTip() {return tip;}
    public void setTip(BigDecimal tip) {this.tip = tip;}

    public BigDecimal getAmountPaid() {return amountPaid;}
    public void setAmountPaid(BigDecimal amountPaid) {this.amountPaid = amountPaid;}

    public PaymentType getPaymentType() {return paymentType;}
    public void setPaymentType(PaymentType paymentType) {this.paymentType = paymentType;}

    public LocalDateTime getOrderDate() {return orderDate;}
    public void setOrderDate(LocalDateTime orderDate) {this.orderDate = orderDate;}

    public LocalDateTime getPickupTime() {return pickupTime;}
    public void setPickupTime(LocalDateTime pickupTime) {this.pickupTime = pickupTime;}

    public List<CustomerPizza> getDetails() {return details;}
    public void setDetails(List<CustomerPizza> orderDetails) {this.details = orderDetails;}

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "id=" + id +
                ", customer=" + customer +
                ", status=" + orderStatus +
                ", orderDate=" + orderDate +
                ", count=" + getPizzaCount() +
                '}';
    }
}