package pl.com.company.service;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.company.controller.EmployeeDto;
import pl.com.company.model.Employee;
import pl.com.company.repository.EmployeeRepo;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    private static final String FIRST_NAME_TEST = "TEST_NAME";
    private static final String LAST_NAME_TEST = "TEST_LAST_NAME";
    private static final String PESEL_TEST = "TEST_PESEL";
    private static final BigDecimal SALARY_TEST = BigDecimal.TEN;


    @Mock
    EmployeeRepo employeeRepo;

    @InjectMocks
    EmployeeServiceImpl employeeService;

    private Employee employeeFromRepo;

    private EmployeeDto employeeDtoFromController;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @BeforeEach
    public void setup() throws Exception {
        this.employeeFromRepo = new Employee(FIRST_NAME_TEST, LAST_NAME_TEST, PESEL_TEST, SALARY_TEST);
        this.employeeDtoFromController = new EmployeeDto(FIRST_NAME_TEST, LAST_NAME_TEST, PESEL_TEST, SALARY_TEST);
    }

    @Test
    void create() throws Exception {

        when(employeeRepo.create(anyString(), anyString(), anyString(), any(BigDecimal.class))).thenReturn(this.employeeFromRepo);

        EmployeeDto employeeDtoFromService = employeeService.create(employeeDtoFromController);

        assertNotNull(employeeDtoFromService);
        assertEquals(FIRST_NAME_TEST, employeeDtoFromService.getFirstName());
        assertEquals(LAST_NAME_TEST, employeeDtoFromService.getLastName());
        assertEquals(PESEL_TEST, employeeDtoFromService.getPesel());
        assertEquals(SALARY_TEST, employeeDtoFromService.getSalary());
        verify(employeeRepo, times(1)).create(anyString(), anyString(), anyString(), any(BigDecimal.class));

    }

    @Test
    void update() {

        when(employeeRepo.update(any(Employee.class))).thenReturn(this.employeeFromRepo);

        EmployeeDto employeeDtoFromService = employeeService.update(employeeDtoFromController);

        assertNotNull(employeeDtoFromService);
        assertEquals(FIRST_NAME_TEST, employeeDtoFromService.getFirstName());
        assertEquals(LAST_NAME_TEST, employeeDtoFromService.getLastName());
        assertEquals(PESEL_TEST, employeeDtoFromService.getPesel());
        assertEquals(SALARY_TEST, employeeDtoFromService.getSalary());
        verify(employeeRepo, times(1)).update(any(Employee.class));

    }

    @Test
    void get() {

        when(employeeRepo.get(anyString())).thenReturn(this.employeeFromRepo);

        EmployeeDto employeeDTO = employeeService.get(PESEL_TEST);

        assertNotNull(employeeDTO);
        assertEquals(FIRST_NAME_TEST, employeeDTO.getFirstName());
        assertEquals(LAST_NAME_TEST, employeeDTO.getLastName());
        assertEquals(PESEL_TEST, employeeDTO.getPesel());
        assertEquals(SALARY_TEST, employeeDTO.getSalary());
        verify(employeeRepo, times(1)).get(anyString());

    }

    @Test
    void delete() {
        when(employeeRepo.delete(anyString())).thenReturn(true);

        boolean deleteResult = employeeService.delete(PESEL_TEST);

        assertTrue(deleteResult);
        verify(employeeRepo, times(1)).delete(anyString());


    }
}