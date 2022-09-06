package pl.com.company.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.company.controller.EmployeeDto;
import pl.com.company.model.Employee;
import pl.com.company.repository.EmployeeRepo;

import java.math.BigDecimal;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    EmployeeRepo employeeRepo;

    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        return convertToEmployeeDTO(this.employeeRepo.create(employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getPesel(), employeeDto.getSalary()));
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
        EmployeeDto employeeDTO = new EmployeeDto();
        BeanUtils.copyProperties(employee, employeeDTO);

        return employeeDTO;
    }

    public Employee convertToEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDto, employee);

        return employee;
    }
}
