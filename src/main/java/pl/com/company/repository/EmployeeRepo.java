package pl.com.company.repository;

import pl.com.company.model.Employee;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeRepo {

    Employee create(String firstName, String lastName, String pesel, BigDecimal salary);

    Employee get(String pesel);

    List<Employee> getAll();

    boolean delete(String pesel);

    Employee update(Employee employee);

    int size();

    boolean clear();
}
