package dramaholic.customer;

import dramaholic.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, JpaRepository<Customer, Long> {
    Customer getCustomerByUsername(String username);
    Customer getCustomerById(Long id);

    Boolean existsCustomerByUsernameIgnoreCase(String username);
    Boolean existsCustomerByUsernameAndPassword(String username, String password);

    Long deleteCustomerByUsernameEquals(String username);

    Customer getCustomerByUsernameAndPassword(String username, String password);

    List<Customer> getCustomerByHistoryContainingOrWatchLaterContaining(Movie movie, Movie movie2);
}