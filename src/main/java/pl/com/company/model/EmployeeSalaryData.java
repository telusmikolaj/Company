package pl.com.company.model;

import javax.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EmployeeSalaryData implements Entity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pesel;

    private int month;

    private int year;
    private String period;
    private BigDecimal monthSalary;
    private LocalDateTime created;
    private LocalDateTime updated;

    public EmployeeSalaryData(String pesel, int month, int year, BigDecimal monthSalary) {
        this.pesel = pesel;
        this.month = month;
        this.year = year;
        this.monthSalary = monthSalary;
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    public EmployeeSalaryData() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public BigDecimal getMonthSalary() {
        return monthSalary;
    }

    public void setMonthSalary(BigDecimal monthSalary) {
        this.monthSalary = monthSalary;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
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

    @Override
    public String getPesel() {
        return this.pesel;
    }
}
