package dramaholic.customer;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository){
            this.customerRepository = customerRepository;
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

    // Get single Customer by Id
    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }


    // Delete a Customer
    public String deleteCustomer(Long id) {
        try{
            customerRepository.deleteById(id);
            return "Deleted";
        }catch(Exception e) {
            return "Failed";
        }
    }


}