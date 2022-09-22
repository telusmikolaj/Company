package pl.com.company.repository;

import org.springframework.stereotype.Repository;
import pl.com.company.exception.EmployeeAlreadyExistsException;
import pl.com.company.exception.EmployeeNotFoundException;
import pl.com.company.model.Employee;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepositoryDefault implements EmployeeRepo {
    private List<Employee> employeeList;

    private final EmployeeSalaryDataRepo employeeSalaryDataRepo;

    public EmployeeRepositoryDefault(EmployeeSalaryDataRepo employeeSalaryDataRepo) {
        this.employeeSalaryDataRepo = employeeSalaryDataRepo;
    }

    @Override
    public Employee create(String firstName, String lastName, String pesel, BigDecimal salary) {

        Employee employee = new Employee(firstName, lastName, pesel, salary);

        if (checkIfEmployeeExists(pesel)) {
            throw new EmployeeAlreadyExistsException(pesel);
        }
        employeeList.add(employee);


        return employee;
    }

    @Override
    public Employee get(String pesel) {
        List <Employee> filteredEmpoloyeeList = this.employeeList.stream()
                .filter(employee -> employee.getPesel().equals(pesel))
                .collect(Collectors.toList());

        if (filteredEmpoloyeeList.size() == 0) {
            throw new EmployeeNotFoundException(pesel);
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
            throw new EmployeeNotFoundException(pesel);
        }

        employeeSalaryDataRepo.deleteAllEmployeeSalaryData(pesel);
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

    public boolean checkIfEmployeeExists(String pesel) {
        if (employeeList.size() == 0) {
            return false;
        }
        return employeeList.stream().anyMatch(employee -> employee.getPesel().equals(pesel));
    }

    public void loadAll(List<Employee> savedList) {
        this.employeeList = savedList;
    }


}
