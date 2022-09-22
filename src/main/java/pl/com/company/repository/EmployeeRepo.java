package pl.com.company.repository;

import org.springframework.stereotype.Repository;
import pl.com.company.model.Employee;
import pl.com.company.model.EmployeeSalaryData;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeRepo {

    Employee create(String firstName, String lastName, String pesel, BigDecimal salary);

    Employee get(String pesel);


    boolean delete(String pesel);

    Employee update(Employee employee);

    int size();

    boolean clear();
    boolean checkIfEmployeeExists(String pesel);

    void loadAll(List<Employee> employeeSalaryDataList);

    List<Employee> getAll();

}
