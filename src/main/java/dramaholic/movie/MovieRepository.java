package dramaholic.movie;

import dramaholic.customer.Customer;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {

}