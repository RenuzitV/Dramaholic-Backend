package dramaholic.customer;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository CustomerRepository;

    // Add new Customer
    public String addCustomer(Customer s) {

        try {
            CustomerRepository.save(s);
            return "saved";
        } catch(Exception e) {
            return "failed";
        }
    }


    // Update a Customer
    public String updateCustomer(Long id, Customer s) {
        try {
            s.setId(id);
            CustomerRepository.save(s);
            return "Updated";
        }catch(Exception e) {
            return "Failed";
        }
    }

    // Get all Customers
    public Iterable<Customer> getAllCustomer(){
        return CustomerRepository.findAll();
    }

    // Get single Customer by Id
    public Optional<Customer> getCustomer(Long id) {
        return CustomerRepository.findById(id);
    }


    // Delete a Customer
    public String deleteCustomer(Long id) {
        try{
            CustomerRepository.deleteById(id);
            return "Deleted";
        }catch(Exception e) {
            return "Failed";
        }
    }


}