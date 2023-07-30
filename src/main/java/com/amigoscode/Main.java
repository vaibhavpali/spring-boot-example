package com.amigoscode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RequestMapping("/api/v1/customers")
@RestController
public class Main {

    private CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @GetMapping
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    record CustomerRequest(
            String name,
            Integer age,
            String email
    ) {
    }

    @PostMapping
    public void addCustomer(@RequestBody CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setAge(customerRequest.age);
        customer.setName(customerRequest.name);
        customer.setEmail(customerRequest.email);
        customerRepository.save(customer);
    }

    @DeleteMapping("{customerId}")
    public void deleteById(@PathVariable("customerId") Integer id) {
        customerRepository.deleteById(id);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Integer id, @RequestBody CustomerRequest customerRequest) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()) {
            Customer customerData = customer.get();
            if(!customerRequest.name.isEmpty()) {
                customerData.setName(customerRequest.name);
            }
            if(customerRequest.age > 0) {
                customerData.setAge(customerRequest.age);
            }
            if(!customerRequest.email.isEmpty()) {
                customerData.setEmail(customerRequest.email);
            }
            customerRepository.save(customerData);
        }

    }

    /*@GetMapping("/greet")
    public GreetResponse greet() {
        return new GreetResponse("Hello", List.of("Java", "GoLang", "Python"), new Person("Alex"));
    }

    record Person(String name) {
    }

    ;

    record GreetResponse(String greet, List<String> programmingLanguages, Person person) {
    }*/
}
