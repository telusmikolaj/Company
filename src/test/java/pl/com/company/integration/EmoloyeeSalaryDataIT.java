package pl.com.company.integration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.company.controller.EmployeeDto;
import pl.com.company.controller.EmployeeSalaryDataDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmoloyeeSalaryDataIT {

    public static final String PESEL_TEST = "76123500000";
    public static final int MONTH_TEST = 12;

    public static final int YEAR_TEST = 2022;
    public static final BigDecimal SALARY_TEST = BigDecimal.ONE;

    public static final String FIRST_NAME_TEST = "1";

    public static final String LAST_NAME_TEST = "1";

    RequestSpecification requestSpecification;

    ResponseSpecification responseSpecification;

    private EmployeeSalaryDataDto salaryDataDto;

    private EmployeeDto employeeDto;

    RequestSpecification requestSpecificationSalaryData;

    @BeforeEach
    public void setUp() {

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("http://localhost:8080/")
                .setBasePath("employee")
                .setContentType(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();


        RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
        ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();
        RestAssured.filters(requestLoggingFilter, responseLoggingFilter);


        this.salaryDataDto = new EmployeeSalaryDataDto(PESEL_TEST, MONTH_TEST, YEAR_TEST, SALARY_TEST);
        this.employeeDto = new EmployeeDto(FIRST_NAME_TEST, LAST_NAME_TEST, PESEL_TEST, SALARY_TEST);


        given()
                .spec(requestSpecification)
                .body(this.employeeDto).
                when()
                .post().
                then()
                .spec(responseSpecification)
                .extract().body().as(EmployeeDto.class);

        requestSpecificationSalaryData = new RequestSpecBuilder()
                .setBaseUri("http://localhost:8080/")
                .setBasePath("salary")
                .setContentType(ContentType.JSON)
                .build();

        sendPostRequest(this.salaryDataDto);

    }

    @AfterEach
    public void tearDown() {

        given()
                .pathParam("pesel", PESEL_TEST)
                .spec(requestSpecification).
                when()
                .delete("{pesel}").
                then()
                .spec(responseSpecification);
    }

    @Test
    void create() {

        EmployeeSalaryDataDto newSalaryDataDto = new EmployeeSalaryDataDto(PESEL_TEST, 12, 20220, BigDecimal.ONE);

        EmployeeSalaryDataDto savedSalaryDto = sendPostRequest(newSalaryDataDto);

        assertNotNull(savedSalaryDto);
        assertEquals(newSalaryDataDto.getPesel(), savedSalaryDto.getPesel());
        assertEquals(newSalaryDataDto.getMonthSalary() , savedSalaryDto.getMonthSalary());
        assertEquals(newSalaryDataDto.getMonth(), savedSalaryDto.getMonth());
        assertEquals(newSalaryDataDto.getYear(), savedSalaryDto.getYear());
    }

    @Test
    void getSalaryData() {

        EmployeeSalaryDataDto savedSalaryDto = given()
                .pathParam("pesel", PESEL_TEST)
                .pathParam("year", YEAR_TEST)
                .pathParam("month", MONTH_TEST)
                .spec(requestSpecificationSalaryData)
                .when().get("{pesel}/{year}/{month}")
                .then().spec(responseSpecification)
                .extract().body().as(EmployeeSalaryDataDto.class);

        assertNotNull(savedSalaryDto);
        assertEquals(this.salaryDataDto.getPesel(), savedSalaryDto.getPesel());
        assertEquals(this.salaryDataDto.getMonthSalary() , savedSalaryDto.getMonthSalary());
        assertEquals(this.salaryDataDto.getMonth(), savedSalaryDto.getMonth());
        assertEquals(this.salaryDataDto.getYear(), savedSalaryDto.getYear());
    }

    @Test
    void getAllSalaryData() {

        EmployeeSalaryDataDto newSalaryDataDto = new EmployeeSalaryDataDto(PESEL_TEST, 12, 20220, BigDecimal.ONE);

        sendPostRequest(newSalaryDataDto);


        EmployeeSalaryDataDto[] savedSalaryDto = getAllEmployeeSalaryData(PESEL_TEST);

        assertNotNull(savedSalaryDto);
        assertEquals(this.salaryDataDto.getPesel(), savedSalaryDto[0].getPesel());
        assertEquals(this.salaryDataDto.getMonthSalary() , savedSalaryDto[0].getMonthSalary());
        assertEquals(this.salaryDataDto.getMonth(), savedSalaryDto[0].getMonth());
        assertEquals(this.salaryDataDto.getYear(), savedSalaryDto[0].getYear());

        assertEquals(newSalaryDataDto.getPesel(), savedSalaryDto[1].getPesel());
        assertEquals(newSalaryDataDto.getMonthSalary() , savedSalaryDto[1].getMonthSalary());
        assertEquals(newSalaryDataDto.getMonth(), savedSalaryDto[1].getMonth());
        assertEquals(newSalaryDataDto.getYear(), savedSalaryDto[1].getYear());
    }

    @Test
    void deleteAllEmployeeSalaryData() {
        boolean isDeleted = given()
                .pathParam("pesel", PESEL_TEST)
                .spec(requestSpecificationSalaryData).
                when()
                .delete("{pesel}").
                then()
                .spec(responseSpecification)
                .extract().as(Boolean.class);


        assertTrue(isDeleted);
    }

    @Test
    void delete() {
        boolean isDeleted = given()
                .pathParam("pesel", PESEL_TEST)
                .pathParam("year", YEAR_TEST)
                .pathParam("month", MONTH_TEST)
                .spec(requestSpecificationSalaryData).
                when()
                .delete("{pesel}/{year}/{month}").
                then()
                .spec(responseSpecification)
                .extract().as(Boolean.class);

        assertTrue(isDeleted);
    }

    @Test
    void update() {


        EmployeeSalaryDataDto newSalaryDataDto = new EmployeeSalaryDataDto(PESEL_TEST, 12, 20220, BigDecimal.ONE);
        List<EmployeeSalaryDataDto> listForUpdate = new ArrayList<>();
        listForUpdate.add(this.salaryDataDto);
        listForUpdate.add(newSalaryDataDto);

        EmployeeSalaryDataDto updatedDto =
                given().spec(requestSpecificationSalaryData).body(listForUpdate)
                .when().put()
                .then().spec(responseSpecification)
                .extract().body().as(EmployeeSalaryDataDto.class);

        EmployeeSalaryDataDto[] salaryDataDtos = getAllEmployeeSalaryData(PESEL_TEST);

        assertEquals(salaryDataDtos.length, 1);
        assertEquals(updatedDto.getPesel(), salaryDataDtos[0].getPesel());
        assertEquals(updatedDto.getMonth(), salaryDataDtos[0].getMonth());
        assertEquals(updatedDto.getYear(), salaryDataDtos[0].getYear());
        assertEquals(updatedDto.getMonthSalary(), salaryDataDtos[0].getMonthSalary());
    }


    private EmployeeSalaryDataDto sendPostRequest(EmployeeSalaryDataDto dto) {
        return given().spec(requestSpecificationSalaryData)
                .body(dto)
                .when().post()
                .then().spec(responseSpecification)
                .extract().body().as(EmployeeSalaryDataDto.class);
    }

    private EmployeeSalaryDataDto[] getAllEmployeeSalaryData(String pesel) {
        return      given()
                .pathParam("pesel", pesel)
                .spec(requestSpecificationSalaryData)
                .when().get("{pesel}/all")
                .then().spec(responseSpecification)
                .extract().body().as(EmployeeSalaryDataDto[].class);
    }



}
