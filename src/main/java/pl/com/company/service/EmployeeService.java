package pl.com.company.service;

import pl.com.company.controller.EmployeeDto;
import pl.com.company.model.Employee;

import java.math.BigDecimal;

public interface EmployeeService {

    EmployeeDto create(EmployeeDto employeeDto);

    EmployeeDto update(EmployeeDto employeeDto);

    EmployeeDto get(String pesel);

    boolean delete(String pesel);


}
