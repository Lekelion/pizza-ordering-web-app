package pizzaTime.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaCrustRepo extends JpaRepository<PizzaCrust, Integer> {
}