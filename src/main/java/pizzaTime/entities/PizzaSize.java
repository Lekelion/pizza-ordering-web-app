package pizzaTime.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class PizzaSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, length = 16)
    private String name;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    public PizzaSize() {
    }

    public PizzaSize(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}

    @Override
    public String toString() {
        return name + ", $" + price;
    }
}