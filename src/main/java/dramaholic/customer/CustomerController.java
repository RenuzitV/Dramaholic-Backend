package dramaholic.customer;

import dramaholic.movie.Movie;
import dramaholic.movie.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final MovieService movieService;

    @Autowired
    CustomerController(MovieService movieService, CustomerService customerService){
        this.movieService = movieService;
        this.customerService = customerService;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
        if (!customerService.isValid(customer)) return new ResponseEntity<>("not valid", HttpStatus.BAD_REQUEST);
        if (customerService.exists(customer)) return new ResponseEntity<>("existed username", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(customerService.addCustomer(customer), HttpStatus.CREATED);
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<Page<Customer>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "username,desc") String[] sort){
        try {
            List<Order> orders = new ArrayList<>();
            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
            Page<Customer> customerPage = customerService.getAllCustomer(pagingSort);

            HttpHeaders responseHeaders = new HttpHeaders();
            List<MediaType> medias = new ArrayList<>();
            medias.add(MediaType.ALL);
            responseHeaders.setAccept(medias);

            return new ResponseEntity<>(customerPage, responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id){
        String res = customerService.deleteCustomerByID(id);
        if (res.equals("Deleted")) return new ResponseEntity<>("Deleted", HttpStatus.OK);
        else return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id){
        Optional<Customer> optionalCustomer = customerService.getCustomer(id);
        if (optionalCustomer.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(optionalCustomer.get(), HttpStatus.FOUND);
    }

    @PutMapping(value ="/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @RequestBody Customer customer){
        boolean ok = customerService.exists(id);
        if (!ok) return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(customerService.updateCustomer(id, customer), HttpStatus.OK);
    }

    @PostMapping(value = "/login", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Customer> login(@RequestBody Customer customer){
        boolean res = customerService.checkCredentials(customer);
        if (res) return new ResponseEntity<>(customerService.getCustomer(customer), HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value = "/watchlater", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Movie>> getWatchlater(@RequestBody HashMap<String, String> body){
        Customer customer = getCustomerFromService(body);
        if (customer == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(customer.getWatchLater(), HttpStatus.OK);
    }

    @GetMapping(value = "/history", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Movie>> getHistory(@RequestBody HashMap<String, String> body){
        Customer customer = getCustomerFromService(body);
        if (customer == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(customer.getHistory(), HttpStatus.OK);
    }

    @PostMapping(value = "/watchlater", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> addWatchlater(@RequestBody HashMap<String, String> body){
        boolean customerExists = customerService.checkCredentials(body);
        if (!customerExists) return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
        boolean movieExists = movieService.exists(Long.parseLong(body.get("dbID")));
        if (!movieExists) return new ResponseEntity<>("Cannot find movie", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(customerService.setWatchlater(body), HttpStatus.OK);
    }

    @PostMapping(value = "/history", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> addHistory(@RequestBody HashMap<String, String> body){
        boolean customerExists = customerService.checkCredentials(body);
        if (!customerExists) return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
        boolean movieExists = movieService.exists(Long.parseLong(body.get("dbID")));
        if (!movieExists) return new ResponseEntity<>("Cannot find movie", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(customerService.addHistory(body), HttpStatus.OK);
    }

    @DeleteMapping(value = "/watchlater", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> deleteWatchlater(@RequestBody HashMap<String, String> body){
        boolean customerExists = customerService.checkCredentials(body);
        if (!customerExists) return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
        boolean movieExists = movieService.exists(Long.parseLong(body.get("dbID")));
        if (!movieExists) return new ResponseEntity<>("Cannot find movie", HttpStatus.NOT_FOUND);
        customerService.removeWatchlater(body);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @DeleteMapping(value = "/history", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> deleteHistory(@RequestBody HashMap<String, String> body){
        boolean customerExists = customerService.checkCredentials(body);
        if (!customerExists) return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
        boolean movieExists = movieService.exists(Long.parseLong(body.get("dbID")));
        if (!movieExists) return new ResponseEntity<>("Cannot find movie", HttpStatus.NOT_FOUND);
        customerService.removeHistory(body);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    private Customer getCustomerFromService(HashMap<String, String> body){
        Customer customerInfo = new Customer(body.get("username"), body.get("password"));
        if (customerInfo.getUsername() == null || customerInfo.getPassword() == null) return null;
        boolean res = customerService.checkCredentials(customerInfo);
        if (!res) return null;
        return customerService.getCustomer(customerInfo);
    }
}
