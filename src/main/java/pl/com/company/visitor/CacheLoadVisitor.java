package pl.com.company.visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.company.model.Employee;
import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.model.Entity;
import pl.com.company.repository.CacheService;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class CacheLoadVisitor implements Visitor {


    private final String EMPLOYEES_FILE_PATH = "./src/main/resources/data_employees.json";
    private final String EMPLOYEES_SALARY_DATA_FILE_PATH = "./src/main/resources/salary_data.json";
    private final CacheService cacheService;

    public CacheLoadVisitor(CacheService cacheService) {
        this.cacheService = cacheService;
    }
    @Override
    public void visit(Visitable visitable) throws IOException {
        String className = visitable.getClass().getName();

        if (className.contains("EmployeeSalaryData")) {
            visitable.loadAll(cacheService.load(EMPLOYEES_SALARY_DATA_FILE_PATH, "SalaryData"));
        } else {
            visitable.loadAll(cacheService.load(EMPLOYEES_FILE_PATH, "Employees"));
        }
    }
}
