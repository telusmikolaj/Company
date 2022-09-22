package pl.com.company.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.company.exception.EmployeeSalaryDataRequestException;
import pl.com.company.model.EmployeeSalaryData;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeSalaryDataIRepoImplTest {

    public static final String PESEL_TEST = "76123500000";
    public static final int MONTH_TEST = 12;
    public static final int YEAR_TEST = 2022;
    public static final BigDecimal SALARY_TEST = BigDecimal.ONE;

    public static final int MONTH_TEST_2 = 1;

    public static final int YEAR_TEST_2 = 2020;

    public static final BigDecimal SALARY_TEST_2 = BigDecimal.ONE;

    public static final int MONTH_TEST_3 = 5;

    public static final int YEAR_TEST_3 = 2020;

    public static final BigDecimal SALARY_TEST_3 = BigDecimal.ONE;

    public static final String NOT_EXSISTING_PESEL = "85120398120";

    public static final int NOT_EXISTING_MONTH = 10;

    public static final int  NOT_EXISTING_YEAR = 2050;



    @Autowired
    EmployeeSalaryDataRepo employeeSalaryDataRepo;

    private EmployeeSalaryData salaryData;

    private EmployeeSalaryData nextSalaryData;

    @BeforeEach
    void setUp() {
        this.salaryData  = employeeSalaryDataRepo.create(PESEL_TEST, MONTH_TEST, YEAR_TEST, SALARY_TEST);
        this.nextSalaryData = employeeSalaryDataRepo.create(PESEL_TEST, MONTH_TEST_2, YEAR_TEST_2, SALARY_TEST_2);

    }

    @AfterEach
    void tearDown() {
        this.salaryData = null;
        this.nextSalaryData = null;
        this.employeeSalaryDataRepo.deleteAll();
    }

    @Test
    void create() {

        assertEquals(salaryData.getPesel(), PESEL_TEST);
        assertEquals(salaryData.getMonth(), MONTH_TEST);
        assertEquals(salaryData.getYear(), YEAR_TEST);
        assertEquals(salaryData.getMonthSalary(), SALARY_TEST);
    }

    @Test
    void createDuplicateShouldThrowEmployeeSalaryDataRequestException() {

        Assertions.assertThrows(EmployeeSalaryDataRequestException.class, () -> {
            employeeSalaryDataRepo.create(PESEL_TEST, MONTH_TEST, YEAR_TEST, SALARY_TEST);
        });

    }

    @Test
    void update() {
        EmployeeSalaryData newSalaryData = new EmployeeSalaryData(PESEL_TEST, MONTH_TEST_3, YEAR_TEST_3, SALARY_TEST_3);

        EmployeeSalaryData updatedSalaryData = employeeSalaryDataRepo.update(salaryData, newSalaryData);

        assertEquals(updatedSalaryData.getPesel(), newSalaryData.getPesel());
        assertEquals(updatedSalaryData.getMonth(), newSalaryData.getMonth());
        assertEquals(updatedSalaryData.getYear(), newSalaryData.getYear());
        assertEquals(updatedSalaryData.getMonthSalary(), newSalaryData.getMonthSalary());
    }

    @Test
    void updateNotExistingSalaryDataShouldThrowEmployeeSalaryDataRequestException() {
        Assertions.assertThrows(EmployeeSalaryDataRequestException.class, () -> {
            employeeSalaryDataRepo.update(
                    new EmployeeSalaryData(NOT_EXSISTING_PESEL, MONTH_TEST, YEAR_TEST, SALARY_TEST),
                    new EmployeeSalaryData(NOT_EXSISTING_PESEL, MONTH_TEST_2, YEAR_TEST_2, SALARY_TEST_2));
        });
    }

    @Test
    void updateToExistingSalaryDataShouldThrowException() {
        Assertions.assertThrows(EmployeeSalaryDataRequestException.class, () -> {
            employeeSalaryDataRepo.update(salaryData, nextSalaryData);
        });
    }

    @Test
    void getEmployeeSalaryForGivenMonthAndYear() {

        EmployeeSalaryData savedSalaryData = employeeSalaryDataRepo.getEmployeeSalaryForGivenMonthAndYear(PESEL_TEST, YEAR_TEST, MONTH_TEST);

        assertEquals(savedSalaryData.getPesel(), PESEL_TEST);
        assertEquals(savedSalaryData.getYear(), YEAR_TEST);
        assertEquals(savedSalaryData.getMonth(), MONTH_TEST);
        assertEquals(savedSalaryData.getMonthSalary(), SALARY_TEST);

    }

    @Test
    void getNotExistingEmployeeSalaryDataShouldThrowException() {
        Assertions.assertThrows(EmployeeSalaryDataRequestException.class, () -> {
            employeeSalaryDataRepo.getEmployeeSalaryForGivenMonthAndYear(PESEL_TEST, NOT_EXISTING_YEAR, NOT_EXISTING_MONTH);
        });
    }

    @Test
    void getAllEmployeeSalaryData() {

        List<EmployeeSalaryData> allEmployeeSalaryData = employeeSalaryDataRepo.getAllEmployeeSalaryData(PESEL_TEST);

        EmployeeSalaryData fetchedSalaryData  = allEmployeeSalaryData.get(0);
        EmployeeSalaryData nextFetchedSalaryData = allEmployeeSalaryData.get(1);

        assertEquals(2, allEmployeeSalaryData.size());
        assertEquals(salaryData.getMonthSalary(), fetchedSalaryData.getMonthSalary());
        assertEquals(salaryData.getMonth(), fetchedSalaryData.getMonth());
        assertEquals(salaryData.getYear(), fetchedSalaryData.getYear());
        assertEquals(salaryData.getPesel(), fetchedSalaryData.getPesel());

        assertEquals(nextSalaryData.getMonthSalary(), nextFetchedSalaryData.getMonthSalary());
        assertEquals(nextSalaryData.getMonth(),nextFetchedSalaryData.getMonth());
        assertEquals(nextSalaryData.getYear(), nextFetchedSalaryData.getYear());
        assertEquals(nextSalaryData.getPesel(), nextFetchedSalaryData.getPesel());


    }

    @Test
    void getAllForNotExistingEmployeeShouldThrowEmployeeSalaryDataRequestException() {
        Assertions.assertThrows(EmployeeSalaryDataRequestException.class, () -> {
            employeeSalaryDataRepo.getAllEmployeeSalaryData(NOT_EXSISTING_PESEL);
        });
    }

    @Test
    void deleteAllEmployeeSalaryData() {

        boolean result = employeeSalaryDataRepo.deleteAllEmployeeSalaryData(PESEL_TEST);

        assertTrue(result);
        assertEquals(0, this.employeeSalaryDataRepo.size());
    }

    @Test
    void deleteEmployeeSalaryDataForGivenMonthAndYear() {

        boolean result = employeeSalaryDataRepo.deleteEmployeeSalaryDataForGivenMonthAndYear(PESEL_TEST, YEAR_TEST, MONTH_TEST);

        assertTrue(result);
        assertEquals(employeeSalaryDataRepo.size(), 1);
    }

    @Test
    void deleteNotExistingSalaryDataShouldThrowEmployeeSalaryDataRequestException() {
        Assertions.assertThrows(EmployeeSalaryDataRequestException.class, () -> {
            employeeSalaryDataRepo.deleteEmployeeSalaryDataForGivenMonthAndYear(
                    PESEL_TEST, NOT_EXISTING_YEAR, NOT_EXISTING_MONTH);
        });
    }
}