package pl.com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.company.controller.EmployeeDto;
import pl.com.company.exception.EmployeeAlreadyExistsException;
import pl.com.company.exception.EmployeeNotFoundException;
import pl.com.company.mapper.EmployeeMapper;
import pl.com.company.model.Employee;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;
@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private EmployeeSalaryDataRepo employeeSalaryDataRepo;
    @Autowired
    private EmployeeMapper mapper;
    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        if (this.employeeRepo.isEmployeeExists(employeeDto.getPesel())) {
            throw new EmployeeAlreadyExistsException(employeeDto.getPesel());
        }
       Employee employee = this.employeeRepo.create(mapper.dtoToEmployee(employeeDto));

        return mapper.employeeToDto(employee);
    }

    @Override
    public EmployeeDto update(EmployeeDto employeeDto) {
        if (!this.employeeRepo.isEmployeeExists(employeeDto.getPesel())) {
            throw new EmployeeNotFoundException(employeeDto.getPesel());
        }
        return mapper.employeeToDto(this.employeeRepo.update(mapper.dtoToEmployee(employeeDto)));
    }

    @Override
    public EmployeeDto get(String pesel) {

        if (!this.employeeRepo.isEmployeeExists(pesel)) {
            throw new EmployeeNotFoundException(pesel);
        }
        Employee employee = this.employeeRepo.get(pesel).get(0);
        return mapper.employeeToDto(employee);
    }

    @Override
    public boolean delete(String pesel) {
        if (!this.employeeRepo.isEmployeeExists(pesel)) {
            throw new EmployeeNotFoundException(pesel);
        }
        this.employeeSalaryDataRepo.delete(pesel);
        return this.employeeRepo.delete(pesel);
    }
}