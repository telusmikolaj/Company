package pl.com.company.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.company.controller.EmployeeDto;
import pl.com.company.model.Employee;
import pl.com.company.repository.EmployeeRepo;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    EmployeeRepo employeeRepo;

    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        Employee employee = this.employeeRepo.create(employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getPesel(), employeeDto.getSalary());
        return convertToEmployeeDTO(employee);
    }

    @Override
    public EmployeeDto update(EmployeeDto employeeDto) {
        return convertToEmployeeDTO(this.employeeRepo.update(convertToEmployee(employeeDto)));
    }

    @Override
    public EmployeeDto get(String pesel) {
        return convertToEmployeeDTO(this.employeeRepo.get(pesel));
    }

    @Override
    public boolean delete(String pesel) {
        return this.employeeRepo.delete(pesel);
    }

    public EmployeeDto convertToEmployeeDTO(Employee employee) {
        if (Objects.nonNull(employee)) {
            EmployeeDto employeeDTO = new EmployeeDto(employee.getFirstName(),
                    employee.getLastName(),
                    employee.getPesel(),
                    employee.getSalary());


            return employeeDTO;
        }
        return null;
    }

    public Employee convertToEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee(employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getPesel(),
                employeeDto.getSalary());


        return employee;
    }
}
