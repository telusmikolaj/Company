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

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmployeeControllerIT {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    private static final String FIRST_NAME_TEST = "TEST_NAME";
    private static final String LAST_NAME_TEST = "TEST_LAST_NAME";
    private static final String PESEL_TEST = "12345678910";
    private static final BigDecimal SALARY_TEST = BigDecimal.TEN;

    private EmployeeDto employeeDto;

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


        this.employeeDto = new EmployeeDto(FIRST_NAME_TEST, LAST_NAME_TEST, PESEL_TEST, SALARY_TEST);

        given()
                .spec(requestSpecification)
                .body(this.employeeDto).
                when()
                .post().
                then()
                .spec(responseSpecification)
                .extract().body().as(EmployeeDto.class);

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
    public void addEmployee() {

        EmployeeDto newDto = new EmployeeDto("NEW_FIRSTNAME", "NEW_LASTNAME","11111111111",BigDecimal.ONE);

        EmployeeDto savedEmployeeDto = given().spec(requestSpecification).body(newDto)
                .when().post()
                .then().spec(responseSpecification)
                .extract().body().as(EmployeeDto.class);

        assertNotNull(savedEmployeeDto);
        assertEquals(newDto.getFirstName(), savedEmployeeDto.getFirstName());
        assertEquals(newDto.getLastName(), savedEmployeeDto.getLastName());
        assertEquals(newDto.getPesel(), savedEmployeeDto.getPesel());
        assertEquals(newDto.getSalary(), savedEmployeeDto.getSalary());
    }


    @Test
    public void getEmployee() {

        EmployeeDto employeeDto = given().pathParam("pesel", PESEL_TEST).spec(requestSpecification)
                .when().get("{pesel}")
                .then().spec(responseSpecification)
                .extract().body().as(EmployeeDto.class);

        assertNotNull(employeeDto);
        assertEquals(FIRST_NAME_TEST, employeeDto.getFirstName());
        assertEquals(LAST_NAME_TEST, employeeDto.getLastName());
        assertEquals(PESEL_TEST, employeeDto.getPesel());
        assertEquals(SALARY_TEST, employeeDto.getSalary());

    }


    @Test
    public void updateEmployee() {

        EmployeeDto newEmployeeDto = new EmployeeDto("UPDATED_NAME", "UPDATED_LASTNAME", PESEL_TEST, BigDecimal.ONE);

        EmployeeDto updatedDto = given().spec(requestSpecification).body(newEmployeeDto)
                .when().put()
                .then().spec(responseSpecification)
                .extract().body().as(EmployeeDto.class);

        assertNotNull(updatedDto);
        assertEquals(newEmployeeDto.getFirstName(), updatedDto.getFirstName());
        assertEquals(newEmployeeDto.getLastName(), updatedDto.getLastName());
        assertEquals(newEmployeeDto.getPesel(), updatedDto.getPesel());
        assertEquals(newEmployeeDto.getSalary(), updatedDto.getSalary());


    }

    @Test
    public void deleteEmployee() {
        given()
                .pathParam("pesel", PESEL_TEST)
                .spec(requestSpecification).
        when()
                .get("{pesel}").
        then()
                .spec(responseSpecification);

    }
}