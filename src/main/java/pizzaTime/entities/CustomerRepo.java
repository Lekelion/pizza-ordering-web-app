package pizzaTime.entities;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepo extends CrudRepository<Customer, Integer> {
    Customer findById(int id);
    Customer findByUsername(String username);
}
