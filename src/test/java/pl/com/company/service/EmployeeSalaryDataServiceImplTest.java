package pl.com.company.service;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.company.controller.EmployeeSalaryDataDto;
import pl.com.company.exception.EmployeeNotFoundException;
import pl.com.company.exception.EmployeeRequestException;
import pl.com.company.exception.InvalidSalaryDataUpdateFormatException;
import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeSalaryDataServiceImplTest {

    public static final String PESEL_TEST = "76123500000";
    public static final int MONTH_TEST = 12;
    public static final int YEAR_TEST = 2022;
    public static final BigDecimal SALARY_TEST = BigDecimal.ONE;

    public static final int MONTH_TWO = 5;

    public static final int YEAR_TWO = 2023;

    public static final BigDecimal SALARY_TWO = BigDecimal.ONE;

    @Mock
    EmployeeSalaryDataRepo employeeSalaryDataRepo;

    @Mock
    EmployeeRepo employeeRepo;
    @InjectMocks
    EmployeeSalaryDataServiceImpl employeeSalaryDataService;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    private EmployeeSalaryData salaryDataFromRepo;

    private EmployeeSalaryDataDto newSalaryDataFromController;

    private EmployeeSalaryDataDto salaryDataDtoFromController;

    private List<EmployeeSalaryData> employeeSalaryDataList;

    private List<EmployeeSalaryDataDto> salaryListToUpdate;

    @BeforeEach
    public void setup() {
        this.salaryDataFromRepo = new EmployeeSalaryData(PESEL_TEST, MONTH_TEST, YEAR_TEST, SALARY_TEST);
        this.salaryDataDtoFromController = new EmployeeSalaryDataDto(PESEL_TEST, MONTH_TEST, YEAR_TEST, SALARY_TEST);
        this.newSalaryDataFromController = new EmployeeSalaryDataDto(PESEL_TEST, MONTH_TWO, YEAR_TEST, SALARY_TWO);
        this.employeeSalaryDataList = Arrays.asList(salaryDataFromRepo);
        this.salaryListToUpdate = new ArrayList<>(Arrays.asList(this.salaryDataDtoFromController, this.newSalaryDataFromController));
    }

    @Test
    void create() {
        when(employeeSalaryDataRepo.create(anyString(), anyInt(),anyInt(), any(BigDecimal.class)))
                .thenReturn(this.salaryDataFromRepo);

        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(true);

        EmployeeSalaryDataDto salaryDataDto = employeeSalaryDataService.create(salaryDataDtoFromController);

        assertNotNull(salaryDataDto);
        assertEquals(PESEL_TEST, salaryDataDto.getPesel());
        assertEquals(MONTH_TEST, salaryDataDto.getMonth());
        assertEquals(YEAR_TEST, salaryDataDto.getYear());
        assertEquals(SALARY_TEST, salaryDataDto.getMonthSalary());
    }

    @Test
    void createWithNotExistingPeselShouldThrowEmployeeRequestException() {
        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(false);

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            EmployeeSalaryDataDto salaryDataDto = employeeSalaryDataService.create(salaryDataDtoFromController);
        });
    }

    @Test
    void update() {
        when(employeeSalaryDataRepo.update(any(EmployeeSalaryData.class), any(EmployeeSalaryData.class)))
                .thenReturn(this.salaryDataFromRepo);

        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(true);

        EmployeeSalaryDataDto updatedSalaryData = employeeSalaryDataService
                .update(this.salaryListToUpdate);

        assertNotNull(updatedSalaryData);
        assertEquals(PESEL_TEST, updatedSalaryData.getPesel());
        assertEquals(MONTH_TEST, updatedSalaryData.getMonth());
        assertEquals(YEAR_TEST, updatedSalaryData.getYear());
        assertEquals(SALARY_TEST, updatedSalaryData.getMonthSalary());
    }

    @Test
    void updateWithNotExistingPeselShouldThrowEmployeeRequestException() {
        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(false);

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            EmployeeSalaryDataDto updatedSalaryData = employeeSalaryDataService
                    .update(this.salaryListToUpdate);
        });
    }

    @Test
    void updateWithInvalidParamShouldThrowException() {
        salaryListToUpdate.remove(1);
        Assertions.assertThrows(InvalidSalaryDataUpdateFormatException.class, () -> {
            EmployeeSalaryDataDto salaryDataDto = employeeSalaryDataService
                    .update(salaryListToUpdate);
        });
    }


    @Test
    void getEmployeeSalaryDataForGivenMonthAndYear() {
        when(employeeSalaryDataRepo.getEmployeeSalaryForGivenMonthAndYear(anyString(), anyInt(), anyInt())).thenReturn(salaryDataFromRepo);
        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(true);

        EmployeeSalaryDataDto salaryDataDto = employeeSalaryDataService
                .getEmployeeSalaryDataForGivenMonthAndYear(PESEL_TEST, YEAR_TEST, MONTH_TEST);

        assertNotNull(salaryDataDto);
        assertEquals(PESEL_TEST, salaryDataDto.getPesel());
        assertEquals(MONTH_TEST, salaryDataDto.getMonth());
        assertEquals(YEAR_TEST, salaryDataDto.getYear());
        assertEquals(SALARY_TEST, salaryDataDto.getMonthSalary());

    }

    @Test
    void getSalaryForNotExistingEmployeeShouldThrowEmployeeRequestException() {
        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(false);

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            EmployeeSalaryDataDto salaryDataDto = employeeSalaryDataService
                    .getEmployeeSalaryDataForGivenMonthAndYear(PESEL_TEST, YEAR_TEST, MONTH_TEST);
        });
    }

    @Test
    void getAllEmployeeSalaryDataForGivenEmployee() {
        when(employeeSalaryDataRepo.getAllEmployeeSalaryData(anyString())).thenReturn(employeeSalaryDataList);
        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(true);

        List <EmployeeSalaryDataDto> fetchedList = employeeSalaryDataService.getAllEmployeeSalaryDataForGivenEmployee(PESEL_TEST);
        EmployeeSalaryDataDto salaryDataDto = fetchedList.get(0);
        assertNotNull(fetchedList);
        assertEquals(fetchedList.size(), 1);
        assertEquals(PESEL_TEST, salaryDataDto.getPesel());
        assertEquals(MONTH_TEST, salaryDataDto.getMonth());
        assertEquals(YEAR_TEST, salaryDataDto.getYear());
        assertEquals(SALARY_TEST, salaryDataDto.getMonthSalary());

    }

    @Test
    void getAllSalaryDataForNotExisitingEmployeeShouldThrowEmployeeRequestException() {
        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(false);

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            List <EmployeeSalaryDataDto> fetchedList = employeeSalaryDataService.getAllEmployeeSalaryDataForGivenEmployee(PESEL_TEST);
        });
    }

    @Test
    void deleteEmployeeSalaryDataForGivenMonthAndYear() {
        when(employeeSalaryDataRepo.deleteEmployeeSalaryDataForGivenMonthAndYear(anyString(), anyInt(), anyInt())).thenReturn(true);
        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(true);

        boolean result = employeeSalaryDataService.deleteEmployeeSalaryDataForGivenMonthAndYear(PESEL_TEST, YEAR_TEST, MONTH_TEST);

        assertTrue(result);
    }

    @Test
    void deleteSalaryDataForNotExistingEmployeeShouldThrowEmployeeRequestException() {
        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(false);

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            boolean result = employeeSalaryDataService.deleteEmployeeSalaryDataForGivenMonthAndYear(PESEL_TEST, YEAR_TEST, MONTH_TEST);
        });
    }

    @Test
    void deleteAllEmployeeSalaryData() {
        when(employeeSalaryDataRepo.deleteAllEmployeeSalaryData(anyString())).thenReturn(true);
        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(true);

        boolean result = employeeSalaryDataService.deleteAllEmployeeSalaryData(PESEL_TEST);

        assertTrue(result);
    }

    @Test
    void deleteAllForNotExisitingEmployeeShouldEmployeeRequestException() {
        when(employeeRepo.checkIfEmployeeExists(anyString())).thenReturn(false);

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            boolean result = employeeSalaryDataService.deleteAllEmployeeSalaryData(PESEL_TEST);
        });
    }
}