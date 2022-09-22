package pl.com.company.configuration;

import org.springframework.context.annotation.Configuration;
import pl.com.company.model.Employee;
import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.repository.CacheService;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;

@Configuration
public class ApplicationConfig {

    private final CacheService cacheService;
    private final EmployeeRepo employeeRepo;

    private final EmployeeSalaryDataRepo employeeSalaryDataRepo;

    private final String EMPLOYEES_FILE_PATH = "./src/main/resources/data_employees.json";
    private final String EMPLOYEES_SALARY_DATA_FILE_PATH = "./src/main/resources/salary_data.json";

    public ApplicationConfig(CacheService cacheService, EmployeeRepo employeeRepo, EmployeeSalaryDataRepo employeeSalaryDataRepo) {
        this.cacheService = cacheService;
        this.employeeRepo = employeeRepo;
        this.employeeSalaryDataRepo = employeeSalaryDataRepo;
    }

    @PostConstruct
    public void initEmployeeData() throws IOException {
        this.employeeRepo.loadAll((List<Employee>) this.cacheService.load(EMPLOYEES_FILE_PATH, "Employees"));
        this.employeeSalaryDataRepo.loadAll((List<EmployeeSalaryData>) this.cacheService.load(EMPLOYEES_SALARY_DATA_FILE_PATH, "SalaryData"));
    }
    @PreDestroy
    public void saveEmployeeData() throws IOException {
        this.cacheService.saveToFile(EMPLOYEES_FILE_PATH, this.employeeRepo.getAll());
        this.cacheService.saveToFile(EMPLOYEES_SALARY_DATA_FILE_PATH, this.employeeSalaryDataRepo.getAll());
    }
}




