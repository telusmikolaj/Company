package pl.com.company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.com.company.exception.EmployeeNotFoundException;
import pl.com.company.exception.PeselAlreadyExsistException;
import pl.com.company.repository.EmployeeRepo;

import java.math.BigDecimal;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    private static final String FIRST_NAME_TEST = "TEST_NAME";
    private static final String LAST_NAME_TEST = "TEST_LAST_NAME";
    private static final String PESEL_TEST = "12345678910";
    private static final BigDecimal SALARY_TEST = BigDecimal.TEN;

    private static final String NO_EXSISTING_PESEL = "11111111111";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    EmployeeRepo employeeRepo;


    @BeforeEach
    public void setup() {
        this.employeeRepo.create(FIRST_NAME_TEST, LAST_NAME_TEST, PESEL_TEST, SALARY_TEST);
    }

    @AfterEach
    public void tearDown() {
        this.employeeRepo.clear();
    }

    @Test
    void get() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/employee/{pesel}",PESEL_TEST))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        EmployeeDto employeeDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EmployeeDto.class);
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getPesel()).isEqualTo(PESEL_TEST);
    }

    @Test
    void create() throws Exception {

        EmployeeDto employeeDto = new EmployeeDto("NAME", "LASTNAME", "85120609281", BigDecimal.TEN);

        String employeeJSON = objectMapper.writeValueAsString(employeeDto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(employeeDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(employeeDto.getLastName()))
                .andExpect(jsonPath("$.pesel").value(employeeDto.getPesel()))
                .andExpect(jsonPath("$.salary").value(employeeDto.getSalary()));

    }

    @Test
    void update() throws Exception {
        EmployeeDto toUpdate = new EmployeeDto("UPDATED_FIRST_NAME", "UPDATED_LAST_NAME", PESEL_TEST, BigDecimal.TEN);

        String toUpdateJSON = objectMapper.writeValueAsString(toUpdate);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toUpdateJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(toUpdate.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(toUpdate.getLastName()))
                .andExpect(jsonPath("$.pesel").value(toUpdate.getPesel()))
                .andExpect(jsonPath("$.salary").value(toUpdate.getSalary()));
    }

    @Test
    void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee/{pesel}", PESEL_TEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getWithNonExsistingPeselShouldRespondNotFoundAndThrowEmployeeNotFoundException() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/employee/{pesel}",NO_EXSISTING_PESEL))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof EmployeeNotFoundException).isTrue());

    }

    @Test
    void createEmployeeWithExistingPeselShouldRespondBadRequestAndPeselAlreadyExistException() throws Exception {
        EmployeeDto duplicateEmployee = new EmployeeDto(FIRST_NAME_TEST, LAST_NAME_TEST, PESEL_TEST, BigDecimal.TEN);
        String duplicateJSON = objectMapper.writeValueAsString(duplicateEmployee);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof PeselAlreadyExsistException).isTrue());
    }

    @Test
    void createEmployeeWithInvalidPeselFormatShouldRespondBadRequestAndThrowMethodArgumentNotValidException() throws Exception {

        EmployeeDto invalidPeselEmployee = new EmployeeDto(FIRST_NAME_TEST, LAST_NAME_TEST, "123", BigDecimal.TEN);
        String inalidPeselJSON = objectMapper.writeValueAsString(invalidPeselEmployee);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inalidPeselJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof MethodArgumentNotValidException).isTrue());
    }

    @Test
    void deleteWithNonExsistingPeselShouldRespondNotFoundAndThrowEmployeeNotFoundException() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/employee/{pesel}",NO_EXSISTING_PESEL))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof EmployeeNotFoundException).isTrue());

    }

    @Test
    void updateWithNonExsistingPeselShouldRespondNotFoundAndThrowEmployeeNotFoundException() throws Exception {

        EmployeeDto nonExistingEmployee = new EmployeeDto(FIRST_NAME_TEST, LAST_NAME_TEST, NO_EXSISTING_PESEL, BigDecimal.TEN);
        String invalidPeselJSON = objectMapper.writeValueAsString(nonExistingEmployee);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPeselJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof EmployeeNotFoundException).isTrue());
    }

    @Test
    void createEmployeeWithBlankFirstNameShouldRespondBadRequestAndThrowMethodArgumentNotValidException() throws Exception {

        EmployeeDto invalidEmployee = new EmployeeDto("", LAST_NAME_TEST, "123", BigDecimal.TEN);
        String invalidJSON = objectMapper.writeValueAsString(invalidEmployee);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof MethodArgumentNotValidException).isTrue());
    }

    @Test
    void createEmployeeWithBlankLastNameShouldRespondBadRequestAndThrowMethodArgumentNotValidException() throws Exception {

        EmployeeDto invalidEmployee = new EmployeeDto(FIRST_NAME_TEST, "", "123", BigDecimal.TEN);
        String invalidJSON = objectMapper.writeValueAsString(invalidEmployee);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof MethodArgumentNotValidException).isTrue());
    }

    @Test
    void createEmployeeWithBlankPeselShouldRespondBadRequestAndThrowMethodArgumentNotValidException() throws Exception {

        EmployeeDto invalidEmployee = new EmployeeDto(FIRST_NAME_TEST, LAST_NAME_TEST, "", BigDecimal.TEN);
        String invalidJSON = objectMapper.writeValueAsString(invalidEmployee);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof MethodArgumentNotValidException).isTrue());
    }


}
