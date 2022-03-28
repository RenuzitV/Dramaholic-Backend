package dramaholic.movie;

import dramaholic.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.Table;

@Component
@Table(name = "movie")
public class MovieDatabaseLoader implements CommandLineRunner {
    private final MovieRepository repository;

    @Autowired
    public MovieDatabaseLoader(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception{
//        this.repository.save(new Movie("Bilbo Baggins", 19L));
    }
}
