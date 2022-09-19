package pl.com.company.repository;

import org.springframework.stereotype.Repository;
import pl.com.company.aop.Timed;
import pl.com.company.exception.EmployeeRequestException;
import pl.com.company.model.Employee;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepositoryDefault implements EmployeeRepo {

    private List<Employee> employeeList = new ArrayList<>();

    @Override
    @Timed
    public Employee create(String firstName, String lastName, String pesel, BigDecimal salary) {

        Employee employee = new Employee(firstName, lastName, pesel, salary);
        Employee employee1 = this.get(pesel);

        if (employee1 != null) {
            throw new EmployeeRequestException("Pesel already exist " + pesel);
        }

        employeeList.add(employee);

        System.out.println(employee);

        return employee;
    }

    @Override
    public Employee get(String pesel) {
        for (Employee employee : employeeList) {
            if (employee.getPesel().equals(pesel)) {
                return employee;
            }
        }

        return null;
    }

    @Override
    public List<Employee> getAll() {
        return new ArrayList<>(employeeList);
    }

    @Override
    public boolean delete(String pesel) {
        Employee employeeToDelete = this.get(pesel);

        if (null == employeeToDelete) {
            throw new EmployeeRequestException("Employee with pesel: " + pesel + " does not exist");
        }

        return employeeList.remove(this.get(pesel));
    }

    @Override
    public Employee update(Employee employee) {
        Employee oldEmployee = this.get(employee.getPesel());
        int index = this.employeeList.indexOf(oldEmployee);

        Employee set = this.employeeList.set(index, employee);
        return get(employee.getPesel());
    }

    @Override
    public int size() {
        return this.employeeList.size();
    }

    @Override
    public boolean clear() {
        this.employeeList.clear();
        return true;
    }
}
