package pl.com.company.repository;

import pl.com.company.model.Employee;
import pl.com.company.model.Entity;
import pl.com.company.visitor.Visitable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends Visitable<Employee>, Repository<Employee> {

    Employee update(Employee employee);
    boolean isEmployeeExists(String pesel);
}
