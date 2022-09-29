package pl.com.company.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Employee implements Entity {

    private String firstName;
    private String lastName;
    private String pesel;
    private BigDecimal salary;

    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

    public Employee(String firstName, String lastName, String pesel, BigDecimal salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.pesel = pesel;

        this.createAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Employee() {

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

    public void setPesel(String pesel) {
        this.pesel = pesel;
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

    @Override
    public String getPesel() {
        return this.pesel;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


}
