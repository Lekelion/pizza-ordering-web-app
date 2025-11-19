package pizzaTime.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerOrderRepo extends JpaRepository<CustomerOrder, Integer> {
    List<CustomerOrder> findByCustomerId(int customerId);
}