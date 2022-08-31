package pl.com.company.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Employee {

    private final String firstName;
    private final String lastName;
    private final String pesel;
    private final BigDecimal salary;

    private final LocalDateTime createAt;
    private final LocalDateTime updatedAt;

    public Employee(String firstName, String lastName, String pesel, BigDecimal salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.pesel = pesel;

        this.createAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getPesel() {
        return pesel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(pesel, employee.pesel) && Objects.equals(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, pesel, salary);
    }



}
