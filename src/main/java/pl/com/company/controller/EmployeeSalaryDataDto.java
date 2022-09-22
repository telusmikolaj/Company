package pl.com.company.controller;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class EmployeeSalaryDataDto {

    @NotBlank(message = "{pesel.invalidFormat}")
    @Pattern(regexp = "^\\d{11}$", message = "{pesel.invalidFormat}")
    private String pesel;

    @NotNull(message = "{invalid.month}")
    @Min(value = 1, message = "{invalid.month}")
    @Max(value = 12, message = "{invalid.month}")
    int month;

    @NotNull(message = "{invalid.year}")
    @Min(value = 1900, message = "{invalid.year}")
    @Max(value = 2100, message = "{invalid.year}")
    int year;
    @NotNull(message = "{invalid.salary}")
    @Min(value = 1, message = "{invalid.salary}")
    private BigDecimal monthSalary;

    public EmployeeSalaryDataDto(String pesel, int month, int year, BigDecimal monthSalary) {
        this.pesel = pesel;
        this.month = month;
        this.year = year;
        this.monthSalary = monthSalary;
    }

    public EmployeeSalaryDataDto() {

    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getMonthSalary() {
        return monthSalary;
    }

    public void setMonthSalary(BigDecimal monthSalary) {
        this.monthSalary = monthSalary;
    }
}
