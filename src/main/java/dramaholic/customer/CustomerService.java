package dramaholic.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import dramaholic.movie.Movie;
import dramaholic.movie.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, MovieRepository movieRepository){
            this.customerRepository = customerRepository;
            this.movieRepository = movieRepository;
    }

    public boolean isValid(Customer s){
        return s.getName() != null && s.getDob() != null && s.getUsername() != null && s.getPassword() != null;
    }

    // Add new Customer
    public String addCustomer(Customer s) {
        try {
            customerRepository.save(s);
            return "saved";
        } catch(Exception e) {
            return "failed";
        }
    }


    // Update a Customer
    public String updateCustomer(Customer s) {
        try {
            customerRepository.save(s);
            return "Updated";
        }catch(Exception e) {
            return "Failed";
        }
    }

    // Get all Customers
    public Iterable<Customer> getAllCustomer(){
        return customerRepository.findAll();
    }
    // Get all Customers
    public Page<Customer> getAllCustomer(Pageable pageable){
        return customerRepository.findAll(pageable);
    }

    // Get single Customer by Id
    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }

    // Delete a Customer
    public String deleteCustomerByID(Long id) {
        try{
            customerRepository.deleteById(id);
            return "Deleted";
        }catch(Exception e) {
            return "Failed, " + e;
        }
    }


    public Long deleteCustomerByUsername(String username) {
        return customerRepository.deleteCustomerByUsernameEquals(username);
    }

    public boolean checkCredentials(Customer customer) {
        if (customer.getUsername() == null) return false;
        Customer found = customerRepository.getCustomerByUsername(customer.getUsername());
        if (found == null) return false;
        return found.checkCredentials(customer);
    }


    public boolean checkCredentials(HashMap<String, String> body) {
        return customerRepository.existsCustomerByUsernameAndPassword(body.get("username"), body.get("password"));
    }

    public Customer getCustomer(Customer customer){
        return customerRepository.getCustomerByUsernameAndPassword(customer.getUsername(), customer.getPassword());
    }


    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public boolean exists(Customer customer) {
        return customerRepository.existsCustomerByUsernameIgnoreCase(customer.getUsername());
    }

    public void addWatchlater(HashMap<String, String> body) {
        Customer customer = customerRepository.getCustomerByUsernameAndPassword(body.get("username"), body.get("password"));
        Movie movie = movieRepository.findFirstByDbID(Long.parseLong(body.get("dbID")));
        customer.addWatchlater(movie);
        System.out.println(movie);
        System.out.println("ASDASDASD");
        customerRepository.save(customer);
    }

    public void addHistory(HashMap<String, String> body) {
        Customer customer = customerRepository.getCustomerByUsernameAndPassword(body.get("username"), body.get("password"));
        Movie movie = movieRepository.findFirstByDbID(Long.parseLong(body.get("dbID")));
        customer.addHistory(movie);
        customerRepository.save(customer);
    }

    public void removeHistory(HashMap<String, String> body) {
        Customer customer = customerRepository.getCustomerByUsernameAndPassword(body.get("username"), body.get("password"));
        Movie movie = movieRepository.findFirstByDbID(Long.parseLong(body.get("dbID")));
        customer.removeHistory(movie);
        customerRepository.save(customer);
    }

    public void removeWatchlater(HashMap<String, String> body) {
        Customer customer = customerRepository.getCustomerByUsernameAndPassword(body.get("username"), body.get("password"));
        Movie movie = movieRepository.findFirstByDbID(Long.parseLong(body.get("dbID")));
        customer.removeWatchlater(movie);
        customerRepository.save(customer);
    }
}