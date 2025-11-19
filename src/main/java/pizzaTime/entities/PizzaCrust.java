package pizzaTime.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class PizzaCrust {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, length = 24)
    private String name;

    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    public PizzaCrust() {
    }

    public PizzaCrust(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}

    @Override
    public String toString() {
        return name + ", $" + price;
    }
}