package pl.com.company.service;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.company.controller.EmployeeDto;
import pl.com.company.exception.EmployeeAlreadyExistsException;
import pl.com.company.exception.EmployeeNotFoundException;
import pl.com.company.mapper.EmployeeMapper;
import pl.com.company.model.Employee;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class EmployeeServiceImplTest {

    private static final String FIRST_NAME_TEST = "TEST_NAME";
    private static final String LAST_NAME_TEST = "TEST_LAST_NAME";
    private static final String PESEL_TEST = "TEST_PESEL";
    private static final BigDecimal SALARY_TEST = BigDecimal.TEN;

    public static final String FIRST_NAME_TEST_2 = "12";
    public static final String LAST_NAME_TEST_2 = "12";
    public static final String PESEL_TEST_2 = "123456";
    public static final BigDecimal SALARY_TEST_2 = BigDecimal.TEN;


    @Mock
    EmployeeRepo employeeRepo;

    @Mock
    EmployeeSalaryDataRepo employeeSalaryDataRepo;

    @Spy
    EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

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

        when(employeeRepo.create(any())).thenReturn(this.employeeFromRepo);

        EmployeeDto employeeDtoFromService = employeeService.create(employeeDtoFromController);

        assertNotNull(employeeDtoFromService);
        assertEquals(FIRST_NAME_TEST, employeeDtoFromService.getFirstName());
        assertEquals(LAST_NAME_TEST, employeeDtoFromService.getLastName());
        assertEquals(PESEL_TEST, employeeDtoFromService.getPesel());
        assertEquals(SALARY_TEST, employeeDtoFromService.getSalary());
        verify(employeeRepo, times(1)).create(any());

    }

    @Test
    void create2SamePesel() {

        when(employeeRepo.isEmployeeExists(anyString())).thenReturn(true);

        Assertions.assertThrows(EmployeeAlreadyExistsException.class, () -> {
            employeeService.create(new EmployeeDto(FIRST_NAME_TEST, LAST_NAME_TEST, PESEL_TEST, SALARY_TEST));
        });

    }

    @Test
    void update() {

        when(employeeRepo.update(any(Employee.class))).thenReturn(this.employeeFromRepo);
        when(employeeRepo.isEmployeeExists(anyString())).thenReturn(true);

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

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(this.employeeFromRepo);
        when(employeeRepo.get(anyString())).thenReturn(employeeList);
        when(employeeRepo.isEmployeeExists(anyString())).thenReturn(true);

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
        when(employeeRepo.isEmployeeExists(anyString())).thenReturn(true);
        when(employeeSalaryDataRepo.delete(anyString())).thenReturn(true);
        boolean deleteResult = employeeService.delete(PESEL_TEST);

        assertTrue(deleteResult);
        verify(employeeRepo, times(1)).delete(anyString());

    }

    @Test
    void updateNotExistingEmployee() {
        EmployeeDto newEmployeeDto = new EmployeeDto(FIRST_NAME_TEST_2, LAST_NAME_TEST_2, PESEL_TEST_2, SALARY_TEST_2);
        when(employeeRepo.isEmployeeExists(anyString())).thenReturn(false);

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.update(newEmployeeDto);
        });
    }

    @Test
    void getNotFoundTest() {
        when(employeeRepo.isEmployeeExists(anyString())).thenReturn(false);
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            EmployeeDto employee = employeeService.get(PESEL_TEST + "1");
        });
    }
}