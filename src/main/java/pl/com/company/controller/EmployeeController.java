package pl.com.company.controller;

import org.springframework.web.bind.annotation.*;
import pl.com.company.model.Employee;

import java.math.BigDecimal;

@RestController
public class EmployeeController {

    @GetMapping
    public Employee get() {
        return createEmployee();
    }

    @PostMapping
    public Employee create(EmployeeDto dto) {
        return createEmployee();
    }

    @PutMapping
    public Employee update(EmployeeDto dto) {
        return createEmployee();
    }

    @DeleteMapping
    public boolean delete(String pesel) {
        return true;
    }

    private Employee createEmployee() {
        return new Employee("TEST_NAME",
                "TEST_LAST_NAME",
                "TEST_PESEL",
                BigDecimal.valueOf(1000));
    }
}
