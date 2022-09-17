package pl.com.company.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.company.model.Employee;
import pl.com.company.model.EmployeeSalaryData;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CacheTest {

    @Autowired
    CacheService cacheService;

    @Autowired
    ObjectMapper objectMapper;
    private final static String EMPLOYEE_FILE_PATH = "./src/test/java/pl/com/company/repository/test.json";
    private final static String SALARY_DATA_FILE_PATH = "./src/test/java/pl/com/company/repository/salary_test.json";

    private static FileWriter fileWriter;
    private static List<Employee> testEmployeeList;

    private static List<EmployeeSalaryData> employeeSalaryDataList;

    private static Employee testEmployee;

    private static  EmployeeSalaryData testSalaryData;
    @BeforeEach
    public void setup() throws IOException {

        testEmployeeList = new ArrayList<>();
        employeeSalaryDataList = new ArrayList<>();

        testEmployee =  new Employee("John", "Carmack", "76123500000", BigDecimal.ONE);
        Employee secondTestEmployee = new Employee("Mark", "Kovalsky", "97060100000", BigDecimal.ONE);
        Employee thirdTestEmployee = new Employee("Steve", "Wood", "811123000000", BigDecimal.ONE);


        testEmployeeList.add(testEmployee);
        testEmployeeList.add(secondTestEmployee);
        testEmployeeList.add(thirdTestEmployee);

        testSalaryData = new EmployeeSalaryData("76123500000", 10, 12, BigDecimal.ONE);
        employeeSalaryDataList.add(testSalaryData);

        fileWriter = new FileWriter(EMPLOYEE_FILE_PATH);
        fileWriter.write("");
        fileWriter.close();

        fileWriter = new FileWriter(SALARY_DATA_FILE_PATH);
        fileWriter.write("");
        fileWriter.close();
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.delete(Paths.get(EMPLOYEE_FILE_PATH));
        Files.delete(Paths.get(SALARY_DATA_FILE_PATH));
    }

    @Test
    void saveToFile() throws IOException {

        cacheService.saveToFile(EMPLOYEE_FILE_PATH, testEmployeeList);

        String jsonFromFile = new String(getDataFromFile(EMPLOYEE_FILE_PATH));
        List<Employee> savedEmployees = converJsonStringToList(jsonFromFile);
        Employee savedEmployee = savedEmployees.get(0);

        assertEquals(savedEmployee.getFirstName(),testEmployee.getFirstName());
        assertEquals(savedEmployee.getLastName(),testEmployee.getLastName());
        assertEquals(savedEmployee.getPesel(), testEmployee.getPesel());
        assertEquals(savedEmployee.getSalary(),testEmployee.getSalary());

    }

    @Test
    void loadEmployeeFromFile() throws IOException {

        String employeesListJson = objectMapper.writeValueAsString(testEmployeeList);
        fileWriter = new FileWriter(EMPLOYEE_FILE_PATH, true);
        fileWriter.write(employeesListJson);
        fileWriter.close();

        List<Employee> savedEmployees = (List<Employee>) cacheService.load(EMPLOYEE_FILE_PATH, "Employees");


        Employee savedEmployee = savedEmployees.get(0);

        assertEquals(testEmployee.getFirstName(), savedEmployee.getFirstName());
        assertEquals(testEmployee.getLastName(), savedEmployee.getLastName());
        assertEquals(testEmployee.getSalary(), savedEmployee.getSalary());
        assertEquals(testEmployee.getPesel(), savedEmployee.getPesel());
    }


    @Test
    void loadSalaryDataFromFile() throws IOException {

        String employeesListJson = objectMapper.writeValueAsString(employeeSalaryDataList);
        fileWriter = new FileWriter(SALARY_DATA_FILE_PATH, true);
        fileWriter.write(employeesListJson);
        fileWriter.close();

        List<EmployeeSalaryData> savedSalaryData = (List<EmployeeSalaryData>) cacheService.load(SALARY_DATA_FILE_PATH, "SalaryData");

        EmployeeSalaryData salaryData = savedSalaryData.get(0);

        assertEquals(testSalaryData.getMonthSalary(), salaryData.getMonthSalary());
        assertEquals(testSalaryData.getPesel(), salaryData.getPesel());
        assertEquals(testSalaryData.getMonth(), salaryData.getMonth());
        assertEquals(testSalaryData.getYear(), testSalaryData.getYear());
    }

    @Test
    void checkIfFileExists() throws IOException {

        String pathToTest = "./src/test/java/pl/com/company/repository/testPathFile";
        FileWriter fileWriterForPathTest = new FileWriter(pathToTest);
        fileWriterForPathTest.write("test line");
        fileWriterForPathTest.close();

        boolean isFileExists = cacheService.checkIfFileExists(pathToTest);

        assertTrue(isFileExists);
        Files.delete(Paths.get(pathToTest));

    }

    private byte[] getDataFromFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    private List<Employee> converJsonStringToList(String jsonString) throws JsonProcessingException {
        return Arrays.asList(objectMapper.readValue(jsonString, Employee[].class));
    }
}