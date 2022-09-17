package pl.com.company.visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.company.repository.CacheService;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;

import java.io.IOException;

@Component
public class CacheSaveVisitor implements Visitor {

    private final String EMPLOYEES_FILE_PATH = "./src/main/resources/data_employees.json";
    private final String EMPLOYEES_SALARY_DATA_FILE_PATH = "./src/main/resources/salary_data.json";
    private final CacheService cacheService;

    public CacheSaveVisitor(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public void visit(Visitable visitable) throws IOException {
        String className = visitable.getClass().getName();

        if (className.contains("EmployeeSalaryData")) {
            this.cacheService.saveToFile(EMPLOYEES_SALARY_DATA_FILE_PATH, visitable.getAll());
        } else {
           this.cacheService.saveToFile(EMPLOYEES_FILE_PATH, visitable.getAll());
        }
    }
}
