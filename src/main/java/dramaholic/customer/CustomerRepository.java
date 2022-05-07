package dramaholic.customer;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
    Customer getCustomerByUsername(String username);
    Customer getCustomerById(Long id);

    Boolean existsCustomerByUsernameIgnoreCase(String username);
    Boolean existsCustomerByUsernameAndPassword(String username, String password);

    Long deleteCustomerByUsernameEquals(String username);

    Customer getCustomerByUsernameAndPassword(String username, String password);
}