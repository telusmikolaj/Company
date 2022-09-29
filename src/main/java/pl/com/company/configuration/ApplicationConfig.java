package pl.com.company.configuration;

import org.springframework.context.annotation.Configuration;
import pl.com.company.repository.*;
import pl.com.company.visitor.CacheLoadVisitor;
import pl.com.company.visitor.CacheSaveVisitor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class ApplicationConfig {

    private final CacheService cacheService;
    private final EmployeeRepositoryDefault employeeRepo;
    private final EmployeeSalaryDataIRepoImpl employeeSalaryDataRepo;

    public ApplicationConfig(CacheService cacheService, EmployeeRepositoryDefault employeeRepo,
                             EmployeeSalaryDataIRepoImpl employeeSalaryDataRepo) {
        this.cacheService = cacheService;
        this.employeeRepo = employeeRepo;
        this.employeeSalaryDataRepo = employeeSalaryDataRepo;
    }

    @PostConstruct
    public void initEmployeeData() throws IOException {
        CacheLoadVisitor cacheLoadVisitor = new CacheLoadVisitor(cacheService);
        this.employeeRepo.accept(cacheLoadVisitor);
        this.employeeSalaryDataRepo.accept(cacheLoadVisitor);
    }
    @PreDestroy
    public void saveEmployeeData() throws IOException {
        CacheSaveVisitor cacheSaveVisitor = new CacheSaveVisitor(cacheService);
        this.employeeRepo.accept(cacheSaveVisitor);
        this.employeeSalaryDataRepo.accept(cacheSaveVisitor);

    }
}




