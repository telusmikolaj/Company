package pl.com.company.configuration;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.hibernate.validator.constraints.pl.PESEL;
import org.springframework.context.annotation.Configuration;
import pl.com.company.model.Employee;
import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.repository.*;
import pl.com.company.visitor.CacheLoadVisitor;
import pl.com.company.visitor.CacheSaveVisitor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

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




