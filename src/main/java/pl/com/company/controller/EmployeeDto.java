package pl.com.company.controller;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class EmployeeDto {

    @NotBlank(message = "{firstName.notblank}")
    private String firstName;
    @NotBlank(message = "{lastName.notblank}")
    private String lastName;
    @NotBlank(message = "{pesel.notblank}")
    @Pattern(regexp = "^\\d{11}$", message = "{pesel.invalidFormat}")
    @NotEmpty
    @NotNull(message = "pesel cannot be null")
    @Pattern(regexp = "^\\d{11}$", message = "Invalid Pesel format")
    private String pesel;
    @NotNull(message = "{salary.null}")
    @Min(value = 1, message = "{invalid.salary}")
    private BigDecimal salary;


    public EmployeeDto() {

    }

    public EmployeeDto(String firstName, String lastName, String pesel, BigDecimal salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.salary = salary;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }


}
