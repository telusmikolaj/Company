package pl.com.company.repository;

import org.springframework.stereotype.Repository;
import pl.com.company.exception.EmployeeNotFoundException;
import pl.com.company.model.Employee;


import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepositoryDefault extends AbstractRepository<Employee> implements EmployeeRepo {
    private List<Employee> employeeList = new ArrayList<>();

    @Override
    public Employee update(Employee employee) {

        employeeList = this.getAll();
        Employee oldEmployee = this.get(employee.getPesel()).get(0);
        int index = this.employeeList.indexOf(oldEmployee);
        this.employeeList.set(index, employee);

        this.loadAll(this.employeeList);
        return this.get(employee.getPesel()).get(0);
    }

    public boolean isEmployeeExists(String pesel) {
        this.employeeList = this.getAll();
        return employeeList.stream().anyMatch(employee -> employee.getPesel().equals(pesel));
    }

}
