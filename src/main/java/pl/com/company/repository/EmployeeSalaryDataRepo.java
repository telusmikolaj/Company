package pl.com.company.repository;

import pl.com.company.controller.EmployeeSalaryDataDto;
import pl.com.company.model.EmployeeSalaryData;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeSalaryDataRepo {

    EmployeeSalaryData create(String pesel, int month, int year, BigDecimal monthSalary);

    EmployeeSalaryData update(EmployeeSalaryData outdatedSalaryData, EmployeeSalaryData updatedSalaryData);

    EmployeeSalaryData getEmployeeSalaryForGivenMonthAndYear(String pesel, int year, int month);

    List<EmployeeSalaryData> getAllEmployeeSalaryData(String pesel);

    boolean deleteAllEmployeeSalaryData(String pesel);

    boolean deleteEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month);

    void deleteAll();

    boolean isSalaryDataExsists(String pesel, int year, int month);

    int size();

    List<EmployeeSalaryData> getAll();

    void loadAll(List<EmployeeSalaryData> employeeSalaryDataList);

}

