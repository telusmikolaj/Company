package pl.com.company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.com.company.exception.ErrorInfo;
import pl.com.company.model.Employee;
import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeSalaryDataControllerTest {

    public static final String PESEL_TEST = "76123500000";
    public static final int MONTH_TEST = 12;
    public static final int YEAR_TEST = 2022;
    public static final BigDecimal SALARY_TEST = BigDecimal.ONE;

    public static final String NOT_EXSISTING_PESEL = "85120398120";
    public static final int NOT_EXISTING_MONTH = 10;
    public static final int  NOT_EXISTING_YEAR = 2050;

    private static final String FIRST_NAME_ONE = "Johnny";

    private static final String LAST_NAME_ONE = "Bean";

    private static final BigDecimal SALARY_TWO = BigDecimal.ONE;

    public static final int MONTH_TWO = 5;

    public static final int YEAR_TWO = 2023;

    public static final int MONTH_THREE = 7;

    public static final int YEAR_THREE = 2023;

    private EmployeeSalaryDataDto salaryDataDto;

    private List<EmployeeSalaryData> employeeSalaryDataList;
    private EmployeeDto testEmployeeDto;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    EmployeeSalaryDataRepo salaryDataRepo;

    @Autowired
    EmployeeRepo employeeRepo;

    List<EmployeeSalaryDataDto> salaryDtoListForUpdate;

    @BeforeEach
    public void setup() {
        this.salaryDataDto = new EmployeeSalaryDataDto(PESEL_TEST, MONTH_TEST, YEAR_TEST, SALARY_TEST);
        this.employeeRepo.create(new Employee(FIRST_NAME_ONE, LAST_NAME_ONE, PESEL_TEST, SALARY_TEST));
        this.salaryDataRepo.create(new EmployeeSalaryData(PESEL_TEST, MONTH_TEST, YEAR_TEST, SALARY_TEST));
        this.salaryDataRepo.create(new EmployeeSalaryData(PESEL_TEST, MONTH_TWO, YEAR_TWO, SALARY_TWO));

        this.salaryDtoListForUpdate = new ArrayList<>(Arrays.asList(this.salaryDataDto));

    }

    @AfterEach
    public void tearDown() {
        this.employeeRepo.deleteAll();
        this.salaryDataRepo.deleteAll();

    }


    @Test
    void getEmployeeSalaryDataForGivenMonthAndYear() throws Exception {
        MvcResult mvcResult = sendRequest(MockMvcRequestBuilders.get(
                        "/salary/" + PESEL_TEST + "/" + YEAR_TEST + "/" + MONTH_TEST)
                                    .content(String.valueOf(MediaType.APPLICATION_JSON)), HttpStatus.OK);

        EmployeeSalaryDataDto salaryDataDtoResponse = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(),EmployeeSalaryDataDto.class);

        assertNotNull(salaryDataDtoResponse);
        assertEquals(PESEL_TEST, salaryDataDtoResponse.getPesel());
        assertEquals(SALARY_TEST, salaryDataDtoResponse.getMonthSalary());
        assertEquals(MONTH_TEST, salaryDataDtoResponse.getMonth());
        assertEquals(YEAR_TEST, salaryDataDtoResponse.getYear());
    }

    @Test
    void getNotExistingSalaryDataShouldThrowException() throws Exception {
        MvcResult mvcResult = sendRequest(MockMvcRequestBuilders.get(
                        "/salary/" + PESEL_TEST + "/" + NOT_EXISTING_YEAR + "/" + NOT_EXISTING_MONTH)
                .content(String.valueOf(MediaType.APPLICATION_JSON)), HttpStatus.NOT_FOUND);

        ErrorInfo exceptionDtoResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorInfo.class);

        assertEquals("Salary data: pesel: " + PESEL_TEST + " " + NOT_EXISTING_MONTH + "-" + NOT_EXISTING_YEAR + " does not exists!",
                exceptionDtoResponse.getMessage());
    }

    @Test
    void getForNotExistingEmployeeShouldThrowException() throws Exception {
        MvcResult mvcResult = sendRequest(MockMvcRequestBuilders.get(
                        "/salary/" + NOT_EXSISTING_PESEL + "/" + YEAR_TEST+ "/" + MONTH_TEST)
                .content(String.valueOf(MediaType.APPLICATION_JSON)), HttpStatus.NOT_FOUND);

        ErrorInfo exceptionDtoResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorInfo.class);

        assertEquals("An employee with pesel " + NOT_EXSISTING_PESEL + " not found!",
                exceptionDtoResponse.getMessage());
    }

    @Test
    void getAllSalaryDataForGivenEmployee() throws Exception {
        MvcResult mvcResult = sendRequest(MockMvcRequestBuilders.get("/salary/" + PESEL_TEST + "/all")
                .content(String.valueOf(MediaType.APPLICATION_JSON)), HttpStatus.OK);

        List<EmployeeSalaryDataDto> salaryDataDtoResponse = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, EmployeeSalaryDataDto.class));

        EmployeeSalaryDataDto salaryDataDto = salaryDataDtoResponse.get(0);
        EmployeeSalaryDataDto secondSalaryDataDto = salaryDataDtoResponse.get(1);


        assertNotNull(salaryDataDtoResponse);
        assertEquals(salaryDataDtoResponse.size(), 2);
        assertEquals(PESEL_TEST, salaryDataDto.getPesel());
        assertEquals(SALARY_TEST, salaryDataDto.getMonthSalary());
        assertEquals(MONTH_TEST, salaryDataDto.getMonth());
        assertEquals(YEAR_TEST, salaryDataDto.getYear());

        assertEquals(PESEL_TEST, secondSalaryDataDto.getPesel());
        assertEquals(SALARY_TWO, secondSalaryDataDto.getMonthSalary());
        assertEquals(MONTH_TWO, secondSalaryDataDto.getMonth());
        assertEquals(YEAR_TWO, secondSalaryDataDto.getYear());
    }

    @Test
    void getAllForNotExistingEmployeeShouldThrowException() throws Exception {
        MvcResult mvcResult = sendRequest(MockMvcRequestBuilders.get("/salary/" + NOT_EXSISTING_PESEL + "/all")
                .content(String.valueOf(MediaType.APPLICATION_JSON)), HttpStatus.NOT_FOUND);

        ErrorInfo exceptionDtoResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorInfo.class);

        assertEquals("An employee with pesel " + NOT_EXSISTING_PESEL + " not found!",
                exceptionDtoResponse.getMessage());
    }

    @Test
    void deleteEmployeeSalaryDataFormSpecificMonthAndYear() throws Exception {
        MvcResult mvcResult = sendRequest(MockMvcRequestBuilders.delete(
                        "/salary/" + PESEL_TEST + "/" + YEAR_TEST + "/" + MONTH_TEST)
                .content(String.valueOf(MediaType.APPLICATION_JSON)), HttpStatus.OK);

        boolean isDeleted = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Boolean.class);

        assertTrue(isDeleted);
    }

    @Test
    void deleteNotExistingSalaryDataShouldThrowException() throws Exception {
        MvcResult mvcResult = sendRequest(MockMvcRequestBuilders.delete(
                        "/salary/" + PESEL_TEST + "/" + NOT_EXISTING_YEAR + "/" + NOT_EXISTING_MONTH)
                .content(String.valueOf(MediaType.APPLICATION_JSON)), HttpStatus.NOT_FOUND);

        ErrorInfo exceptionDtoResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorInfo.class);

        assertEquals("Salary data: pesel: " + PESEL_TEST + " " + NOT_EXISTING_YEAR + "-" + NOT_EXISTING_MONTH + " does not exists!",
                exceptionDtoResponse.getMessage());
    }

    @Test
    void deleteForNotExistingEmployeeShouldThrowException() throws Exception {
        MvcResult mvcResult = sendRequest(MockMvcRequestBuilders.delete(
                        "/salary/" + NOT_EXSISTING_PESEL + "/" + YEAR_TEST + "/" + MONTH_TEST)
                .content(String.valueOf(MediaType.APPLICATION_JSON)), HttpStatus.NOT_FOUND);

        ErrorInfo exceptionDtoResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorInfo.class);

        assertEquals("An employee with pesel " + NOT_EXSISTING_PESEL + " not found!",
                exceptionDtoResponse.getMessage());
    }

    @Test
    void deleteAllEmployeeSalaryData() throws Exception {
        MvcResult mvcResult = sendRequest(MockMvcRequestBuilders.delete(
                        "/salary/" + PESEL_TEST + "/")
                .content(String.valueOf(MediaType.APPLICATION_JSON)), HttpStatus.OK);

        boolean isDeleted = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Boolean.class);

        assertTrue(isDeleted);
        assertEquals(this.salaryDataRepo.size(), 0);
    }

    @Test
    void deleteAllForNotExistingEmployee() throws Exception {
        MvcResult mvcResult = sendRequest(MockMvcRequestBuilders.delete(
                        "/salary/" + NOT_EXSISTING_PESEL + "/")
                .content(String.valueOf(MediaType.APPLICATION_JSON)), HttpStatus.NOT_FOUND);

        ErrorInfo exceptionDtoResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorInfo.class);

        assertEquals("An employee with pesel " + NOT_EXSISTING_PESEL + " not found!",
                exceptionDtoResponse.getMessage());
    }

    @Test
    void create() throws Exception {

        String newSalaryDtoJson = objectMapper.writeValueAsString(
                new EmployeeSalaryDataDto(PESEL_TEST, MONTH_THREE, YEAR_THREE, BigDecimal.ONE));

        MvcResult result = sendRequest(MockMvcRequestBuilders.post("/salary")
                .content(newSalaryDtoJson).contentType(MediaType.APPLICATION_JSON), HttpStatus.OK);

        EmployeeSalaryDataDto newSalaryDto = objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeSalaryDataDto.class);

        assertNotNull(newSalaryDto);
        assertEquals(PESEL_TEST, newSalaryDto.getPesel());
        assertEquals(MONTH_THREE, newSalaryDto.getMonth());
        assertEquals(YEAR_THREE, newSalaryDto.getYear());
    }

    @Test
    void createWithNotExistingEmployeeShouldThrowException() throws Exception {
        String newSalaryDtoJson = objectMapper.writeValueAsString(
                new EmployeeSalaryDataDto(NOT_EXSISTING_PESEL, MONTH_THREE, YEAR_THREE, BigDecimal.ONE));

        MvcResult result = sendRequest(MockMvcRequestBuilders.post("/salary")
                .content(newSalaryDtoJson).contentType(MediaType.APPLICATION_JSON), HttpStatus.NOT_FOUND);

        ErrorInfo exceptionDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorInfo.class);

        assertEquals("An employee with pesel " + NOT_EXSISTING_PESEL + " not found!",
                exceptionDtoResponse.getMessage());
    }

    @Test
    void createDuplicateSalaryDataShouldThrowException() throws  Exception {
        String duplicateSalaryAsJson = objectMapper.writeValueAsString(
                new EmployeeSalaryDataDto(PESEL_TEST, MONTH_TEST, YEAR_TEST, SALARY_TEST));

        MvcResult result = sendRequest(MockMvcRequestBuilders.post("/salary")
                .content(duplicateSalaryAsJson).contentType(MediaType.APPLICATION_JSON), HttpStatus.BAD_REQUEST);

        ErrorInfo exceptionDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorInfo.class);

        assertEquals("Salary data: pesel: " + PESEL_TEST + " " + MONTH_TEST + "-" + YEAR_TEST + " already exists!",
                exceptionDtoResponse.getMessage());
    }

    @Test
    void update() throws Exception {

        this.salaryDtoListForUpdate.add(new EmployeeSalaryDataDto(PESEL_TEST, 10, 2025, BigDecimal.ONE));

        String updateListAsJson = objectMapper.writeValueAsString(this.salaryDtoListForUpdate);

        MvcResult result = sendRequest(MockMvcRequestBuilders.put("/salary")
                .content(updateListAsJson).contentType(MediaType.APPLICATION_JSON), HttpStatus.OK);

        EmployeeSalaryDataDto updatedDTO = objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeSalaryDataDto.class);

        assertNotNull(updatedDTO);
        assertEquals(PESEL_TEST, updatedDTO.getPesel());
        assertEquals(10, updatedDTO.getMonth());
        assertEquals(2025, updatedDTO.getYear());
    }


    @Test
    void updateNotExistingSalaryDataShouldThrowException() throws Exception {
        this.salaryDtoListForUpdate.clear();

        this.salaryDtoListForUpdate.add(new EmployeeSalaryDataDto(PESEL_TEST, NOT_EXISTING_MONTH, NOT_EXISTING_YEAR, SALARY_TEST));
        this.salaryDtoListForUpdate.add(this.salaryDataDto);

        String updateListAsJson = objectMapper.writeValueAsString(this.salaryDtoListForUpdate);

        MvcResult result = sendRequest(MockMvcRequestBuilders.put("/salary")
                .content(updateListAsJson).contentType(MediaType.APPLICATION_JSON), HttpStatus.NOT_FOUND);

        ErrorInfo exceptionDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorInfo.class);

        assertEquals("Salary data: pesel: " + PESEL_TEST + " "+ NOT_EXISTING_MONTH + "-" + NOT_EXISTING_YEAR + " does not exists!",
                exceptionDtoResponse.getMessage());
    }

    @Test
    void updateToExistingSalaryDataShouldThrowException() throws Exception {
        this.salaryDtoListForUpdate.clear();
        this.salaryDtoListForUpdate.add(this.salaryDataDto);
        this.salaryDtoListForUpdate.add(this.salaryDataDto);

        String updateListAsJson = objectMapper.writeValueAsString(this.salaryDtoListForUpdate);

        MvcResult result = sendRequest(MockMvcRequestBuilders.put("/salary")
                .content(updateListAsJson).contentType(MediaType.APPLICATION_JSON), HttpStatus.BAD_REQUEST);

        ErrorInfo exceptionDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorInfo.class);

        assertEquals("Salary data: pesel: " + this.salaryDataDto.getPesel() + " " +
                        this.salaryDataDto.getMonth() + "-" + this.salaryDataDto.getYear() + " already exists!", exceptionDtoResponse.getMessage());

    }


    private MvcResult sendRequest(RequestBuilder request, HttpStatus expectedStatus) throws Exception {
        return mockMvc.perform(request)
                .andExpect(status().is(expectedStatus.value()))
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }
}