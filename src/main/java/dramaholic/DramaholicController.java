package dramaholic;

import dramaholic.customer.Customer;
import dramaholic.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController 
public class DramaholicController {
    @GetMapping(value="/")
    public String hi(){
        return "hi";
    }

    @GetMapping("/hello")
    public String hello(){
        return  "hello!";
    }

    @GetMapping("/hello/{name}")
    public String helloName(@PathVariable(name="name") String name){
        return  "hello " + name;
    }
}
