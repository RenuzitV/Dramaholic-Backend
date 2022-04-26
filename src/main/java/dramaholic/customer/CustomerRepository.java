package dramaholic.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
    Customer getCustomerByUsername(String username);
    Customer getCustomerById(Long id);

    Boolean existsCustomerByUsername(String username);
    Boolean existsCustomerByUsernameAndPassword(String username, String password);

    Long deleteCustomerByUsernameEquals(String username);

    Customer getCustomerByUsernameAndPassword(String username, String password);
}