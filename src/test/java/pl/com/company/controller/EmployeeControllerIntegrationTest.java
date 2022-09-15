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
import pl.com.company.exception.EmployeeException;
import pl.com.company.service.EmployeeService;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIntegrationTest {

    private static final String FIRST_NAME_ONE = "Johnny";
    private static final String LAST_NAME_ONE = "Bean";
    private static final String PESEL_ONE = "0";
    private static final BigDecimal SALARY_ONE = BigDecimal.ONE;

    private static final String FIRST_NAME_TWO = "Rowan";
    private static final String LAST_NAME_TWO = "Atkinson";
    private static final String PESEL_TWO = "1";
    private static final BigDecimal SALARY_TWO = BigDecimal.TEN;

    private EmployeeDto employeeDto;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        EmployeeDto employeeDto = new EmployeeDto(FIRST_NAME_ONE, LAST_NAME_ONE, PESEL_ONE, SALARY_ONE);
        this.employeeDto = this.employeeService.create(employeeDto);
    }

    @AfterEach
    public void clear() {
        if (null != this.employeeService.get(PESEL_ONE)) {
            this.employeeService.delete(PESEL_ONE);
            this.employeeDto = null;
        }
    }

    @Test
    void get() throws Exception {
        MvcResult result = sendRequest(MockMvcRequestBuilders.get("/employee/" + PESEL_ONE).contentType(MediaType.APPLICATION_JSON), HttpStatus.OK);

        EmployeeDto employeeDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeDto.class);

        assertNotNull(employeeDtoResponse);
        assertEquals(this.employeeDto.getFirstName(), employeeDtoResponse.getFirstName());
        assertEquals(this.employeeDto.getLastName(), employeeDtoResponse.getLastName());
        assertEquals(this.employeeDto.getPesel(), employeeDtoResponse.getPesel());
        assertEquals(this.employeeDto.getSalary(), employeeDtoResponse.getSalary());
    }

    @Test
    void create() throws Exception {
        String employeeDtoAsJson = objectMapper.writeValueAsString(new EmployeeDto(FIRST_NAME_TWO, LAST_NAME_TWO, PESEL_TWO, SALARY_TWO));
        MvcResult result = sendRequest(MockMvcRequestBuilders.post("/employee").content(employeeDtoAsJson).contentType(MediaType.APPLICATION_JSON), HttpStatus.OK);

        EmployeeDto employeeDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeDto.class);

        assertNotNull(employeeDtoResponse);
        assertEquals(FIRST_NAME_TWO, employeeDtoResponse.getFirstName());
        assertEquals(LAST_NAME_TWO, employeeDtoResponse.getLastName());
        assertEquals(PESEL_TWO, employeeDtoResponse.getPesel());
        assertEquals(SALARY_TWO, employeeDtoResponse.getSalary());
    }

    @Test
    void cannotCreateSecondEmployeeWithExistingPesel() throws Exception {
        String employeeDtoAsJson = objectMapper.writeValueAsString(new EmployeeDto(FIRST_NAME_TWO, LAST_NAME_TWO, PESEL_ONE, SALARY_TWO));
        MvcResult result = sendRequest(MockMvcRequestBuilders.post("/employee").content(employeeDtoAsJson).contentType(MediaType.APPLICATION_JSON), HttpStatus.CONFLICT);

        EmployeeException exceptionDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeException.class);
        assertEquals("Pesel already exist " + PESEL_ONE, exceptionDtoResponse.getMessage());
    }

    @Test
    void update() throws Exception {
        String employeeDtoAsJson = objectMapper.writeValueAsString(new EmployeeDto(FIRST_NAME_TWO, LAST_NAME_TWO, PESEL_ONE, SALARY_TWO));
        MvcResult result = sendRequest(MockMvcRequestBuilders.put("/employee").content(employeeDtoAsJson).contentType(MediaType.APPLICATION_JSON), HttpStatus.OK);

        EmployeeDto employeeDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeDto.class);

        assertNotNull(employeeDtoResponse);
        assertEquals(FIRST_NAME_TWO, employeeDtoResponse.getFirstName());
        assertEquals(LAST_NAME_TWO, employeeDtoResponse.getLastName());
        assertEquals(PESEL_ONE, employeeDtoResponse.getPesel());
        assertEquals(SALARY_TWO, employeeDtoResponse.getSalary());
    }

    @Test
    void delete() throws Exception {
        MvcResult result = sendRequest(MockMvcRequestBuilders.delete("/employee/{pesel}", PESEL_ONE).contentType(MediaType.APPLICATION_JSON), HttpStatus.OK);

        boolean isDeleted = objectMapper.readValue(result.getResponse().getContentAsString(), Boolean.class);

        assertTrue(isDeleted);
    }

    @Test
    void cannotDeleteNotExistingEmployee() throws Exception {
        sendRequest(MockMvcRequestBuilders.delete("/employee/{pesel}", PESEL_ONE).contentType(MediaType.APPLICATION_JSON), HttpStatus.OK);

        MvcResult result = sendRequest(MockMvcRequestBuilders.delete("/employee/{pesel}", PESEL_ONE).contentType(MediaType.APPLICATION_JSON), HttpStatus.CONFLICT);
        EmployeeException employeeDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeException.class);

        assertEquals("Employee with pesel: " + PESEL_ONE + " does not exist", employeeDtoResponse.getMessage());

    }

    private MvcResult sendRequest(RequestBuilder request, HttpStatus expectedStatus) throws Exception {
        return mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }
}