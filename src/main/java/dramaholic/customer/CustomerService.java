package dramaholic.customer;

import dramaholic.comment.CommentRepository;
import dramaholic.movie.Movie;
import dramaholic.movie.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final MovieRepository movieRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, MovieRepository movieRepository, CommentRepository commentRepository) {
        this.customerRepository = customerRepository;
        this.movieRepository = movieRepository;
        this.commentRepository = commentRepository;
    }


    public boolean isValid(Customer s){
        return s.getName() != null && s.getDob() != null && s.getUsername() != null && s.getPassword() != null;
    }

    // Add new Customer
    public String addCustomer(Customer s) {
        try {
            return customerRepository.save(s).getId().toString();
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

    // Update a Customer
    public String updateCustomer(Long id, Customer s) {
        try {
            Customer customer = customerRepository.getCustomerById(id);
            if (s.getDob() != null) customer.setDob(s.getDob());
            if (s.getEmail() != null) customer.setEmail(s.getEmail());
            if (s.getName() != null) customer.setName(s.getName());
            if (s.getPassword() != null) customer.setPassword(s.getPassword());
            customerRepository.save(customer);
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

    public boolean exists(Long id){
        return customerRepository.existsById(id);
    }

    // Delete a Customer
    public String deleteCustomerByID(Long id) {
        try{
            List<Movie> movies = movieRepository.findMoviesByComments_User_Id(id);
            movies.forEach((movie -> movie.getComments().removeIf(comment -> (comment.getUser().getId().equals(id)))));
            movieRepository.saveAll(movies);
            commentRepository.deleteCommentsByUser_Id(id);
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
        if (body.get("username") == null || body.get("password") == null) return false;
        return customerRepository.existsCustomerByUsernameAndPassword(body.get("username"), body.get("password"));
    }

    public Customer getCustomer(Customer customer){
        if (customer.getUsername() == null || customer.getPassword() == null) return null;
        return customerRepository.getCustomerByUsernameAndPassword(customer.getUsername(), customer.getPassword());
    }


    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public boolean exists(Customer customer) {
        return customerRepository.existsCustomerByUsernameIgnoreCase(customer.getUsername());
    }

    public String setWatchlater(HashMap<String, String> body) {
        Customer customer = customerRepository.getCustomerByUsernameAndPassword(body.get("username"), body.get("password"));
        Movie movie = movieRepository.findFirstByDbID(Long.parseLong(body.get("dbID")));
        if (customer.getWatchLater().contains(movie)) return "already has this in watch later";
        customer.addWatchlater(movie);
        customerRepository.save(customer);
        return "added";
    }

    public String addHistory(HashMap<String, String> body) {
        Customer customer = customerRepository.getCustomerByUsernameAndPassword(body.get("username"), body.get("password"));
        Movie movie = movieRepository.findFirstByDbID(Long.parseLong(body.get("dbID")));
        if (customer.getHistory().contains(movie)) return "already has this in history";
        customer.addHistory(movie);
        customerRepository.save(customer);
        return "added";
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

    public Customer getCustomerFromCredentials(Customer customer) {
        if (customer == null) return null;
        return customerRepository.getCustomerByUsernameAndPassword(customer.getUsername(), customer.getPassword());
    }
}