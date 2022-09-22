package pl.com.company.service;

import pl.com.company.controller.EmployeeSalaryDataDto;
import pl.com.company.model.EmployeeSalaryData;


import java.util.List;


public interface EmployeeSalaryDataService {

    EmployeeSalaryDataDto create(EmployeeSalaryDataDto employeeSalaryDataDto);
    EmployeeSalaryDataDto update(List<EmployeeSalaryDataDto> employeeSalaryDataDtoList);
    EmployeeSalaryDataDto getEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month);
    boolean deleteEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month);

    List<EmployeeSalaryDataDto> getAllEmployeeSalaryDataForGivenEmployee(String pesel);

    boolean deleteAllEmployeeSalaryData(String pesel);


}
