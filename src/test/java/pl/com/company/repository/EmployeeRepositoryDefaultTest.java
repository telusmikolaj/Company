package pl.com.company.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.company.exception.EmployeeAlreadyExistsException;
import pl.com.company.exception.EmployeeNotFoundException;
import pl.com.company.exception.EmployeeRequestException;
import pl.com.company.exception.EmployeeSalaryDataNotFoundException;
import pl.com.company.model.Employee;

import java.math.BigDecimal;
import java.util.List;

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

    private Employee employee1;

    @BeforeEach
    void setUp() {
        this.employee = employeeRepo.create(new Employee(FIRST_NAME_TEST, LAST_NAME_TEST, PESEL_TEST, SALARY_TEST));
        employee1 = employeeRepo.create(new Employee(FIRST_NAME_TEST_2, LAST_NAME_TEST_2, PESEL_TEST_2, SALARY_TEST_2));

    }

    @AfterEach
    void tearDown() {
        this.employee = null;
        this.employeeRepo.deleteAll();
    }

    @Test
    void create() {

        assertEquals(employee.getFirstName(), FIRST_NAME_TEST);
        assertEquals(employee.getLastName(), LAST_NAME_TEST);
        assertEquals(employee.getSalary(), SALARY_TEST);

        employee.toString();
    }

    @Test
    void get() {
        List<Employee> employeeList = employeeRepo.get(PESEL_TEST);

        assertEquals(employee, employeeList.get(0));
    }

    @Test
    void delete() {
        assertEquals(employeeRepo.size(), 2);

        employeeRepo.delete(employee.getPesel());

        assertEquals(employeeRepo.size(), 1);

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
    void isEmployeeExsists() {
        assertTrue(employeeRepo.isEmployeeExists(PESEL_TEST));
        assertFalse(employeeRepo.isEmployeeExists("123"));
    }

    @Test
    void loadAll() {
        List<Employee> employeeList = employeeRepo.getAll();

        assertEquals(employeeList.size(), 2);

    }

    @Test
    void getAll() {
        List<Employee> employeeList = employeeRepo.getAll();

        assertEquals(employee, employeeList.get(0));
        assertEquals(employee1, employeeList.get(1));
    }

    @Test
    void deleteAll() {
        employeeRepo.deleteAll();
        assertEquals(employeeRepo.size(), 0);
    }

    @Test
    void size() {
        assertEquals(employeeRepo.size(),2);
    }


}