package pl.com.company.configuration;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.com.company.model.Employee;
import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
@Component
@Profile("dev")
public class TestDataLoader {

    private final EmployeeRepo employeeRepo;
    private final EmployeeSalaryDataRepo employeeSalaryDataRepo;

    public TestDataLoader(EmployeeRepo employeeRepo, EmployeeSalaryDataRepo employeeSalaryDataRepo) {
        this.employeeRepo = employeeRepo;
        this.employeeSalaryDataRepo = employeeSalaryDataRepo;
        generateTestData();
    }

    public void generateTestData() {

        RandomDataGenerator random = new RandomDataGenerator();
        int[] months = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        int[] years = {
                random.nextInt(1900,2050),
                random.nextInt(1900,2050),
                random.nextInt(1900,2050),
                random.nextInt(1900,2050),
                random.nextInt(1900,2050)
        };

        List<Employee> employeeList = IntStream.range(0, 1000)
                .mapToObj(employee -> new Employee(
                        randomAlphanumeric(random.nextInt(2,10)),
                        randomAlphanumeric(random.nextInt(2,10)),
                        randomNumeric(11,11),
                        BigDecimal.ONE))
                .collect(Collectors.toList());

        List<EmployeeSalaryData> salaryDataList = new ArrayList<>();

        employeeList.forEach(employee -> Arrays.stream(months).forEach(
                month -> Arrays.stream(years).forEach(
                        year -> salaryDataList.add(new EmployeeSalaryData(employee.getPesel(), month, year, employee.getSalary()))
                )
        ));

        employeeRepo.loadAll(employeeList);
        employeeSalaryDataRepo.loadAll(salaryDataList);
    }
}
