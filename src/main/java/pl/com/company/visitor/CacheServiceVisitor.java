package pl.com.company.visitor;

import pl.com.company.model.Employee;
import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;

import java.io.IOException;
import java.util.List;

public interface CacheServiceVisitor {

    void saveEmployeeRepo(EmployeeRepo employeeRepo) throws IOException;
    void loadEmployeeRepo(EmployeeRepo employeeRepo) throws IOException;

    void saveSalaryDataRepo(EmployeeSalaryDataRepo employeeSalaryDataRepo) throws IOException;
    void loadSalaryDataRepo(EmployeeSalaryDataRepo employeeSalaryDataRepo) throws IOException;

}
