package dramaholic.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomerDatabaseLoader implements CommandLineRunner {
    private final CustomerRepository repository;

    @Autowired
    public CustomerDatabaseLoader(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception{
//        this.repository.save(new Customer("Bilbo Baggins", 19L));
    }
}
