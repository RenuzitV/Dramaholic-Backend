package dramaholic.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
    Long deleteCustomerByUsernameEquals(String username);
}