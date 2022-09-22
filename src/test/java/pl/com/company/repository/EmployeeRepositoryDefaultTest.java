package pl.com.company.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.company.exception.EmployeeAlreadyExistsException;
import pl.com.company.exception.EmployeeNotFoundException;
import pl.com.company.exception.EmployeeRequestException;
import pl.com.company.exception.EmployeeSalaryDataNotFoundException;
import pl.com.company.model.Employee;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmployeeRepositoryDefaultTest {

    public static final String FIRST_NAME_TEST = "1";
    public static final String LAST_NAME_TEST = "1";
    public static final String PESEL_TEST = "1234";
    public static final BigDecimal SALARY_TEST = BigDecimal.ONE;

    public static final String FIRST_NAME_TEST_2 = "12";
    public static final String LAST_NAME_TEST_2 = "12";
    public static final String PESEL_TEST_2 = "123456";
    public static final BigDecimal SALARY_TEST_2 = BigDecimal.TEN;

    @Autowired
    private EmployeeRepo employeeRepo;

    private Employee employee;

    @BeforeEach
    void setUp() {
        this.employee = employeeRepo.create(FIRST_NAME_TEST, LAST_NAME_TEST, PESEL_TEST, SALARY_TEST);
    }

    @AfterEach
    void tearDown() {
        this.employee = null;
        this.employeeRepo.clear();
    }

    @Test
    void create() {

        assertEquals(employee.getFirstName(), FIRST_NAME_TEST);
        assertEquals(employee.getLastName(), LAST_NAME_TEST);
        assertEquals(employee.getSalary(), SALARY_TEST);

        employee.toString();
    }

    @Test
    void create2SamePesel() {

        Assertions.assertThrows(EmployeeAlreadyExistsException.class, () -> {
            employeeRepo.create(FIRST_NAME_TEST, LAST_NAME_TEST, PESEL_TEST, SALARY_TEST);
        });

    }

    @Test
    void get() {
        Employee employee1 = employeeRepo.get(PESEL_TEST);

        assertEquals(employee, employee1);
    }

    @Test
    void getNotFoundTest() {

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            Employee employee1 = employeeRepo.get(PESEL_TEST + "1");
        });

    }

    @Test
    void delete() {
        assertEquals(employeeRepo.size(), 1);

        employeeRepo.delete(employee.getPesel());
        assertEquals(employeeRepo.size(), 0);

    }

    @Test
    void update() {
        Employee newEmployee = new Employee(FIRST_NAME_TEST_2, LAST_NAME_TEST_2, PESEL_TEST, SALARY_TEST_2);

        Employee updatedEmployee = employeeRepo.update(newEmployee);

        assertEquals(updatedEmployee.getFirstName(), FIRST_NAME_TEST_2);
        assertEquals(updatedEmployee.getLastName(), LAST_NAME_TEST_2);
        assertEquals(updatedEmployee.getSalary(), SALARY_TEST_2);
    }

    @Test
    void updateNotExistingEmployee() {
        Employee newEmployee = new Employee(FIRST_NAME_TEST_2, LAST_NAME_TEST_2, PESEL_TEST_2, SALARY_TEST_2);

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            employeeRepo.update(newEmployee);
        });
    }
}