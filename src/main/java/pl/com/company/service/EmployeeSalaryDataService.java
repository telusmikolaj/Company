package pl.com.company.service;

import org.quartz.SchedulerException;
import pl.com.company.controller.EmployeeSalaryDataDto;
import pl.com.company.model.EmployeeSalaryData;


import java.text.ParseException;
import java.util.List;


public interface EmployeeSalaryDataService {

    EmployeeSalaryDataDto create(EmployeeSalaryDataDto employeeSalaryDataDto);
    EmployeeSalaryDataDto update(List<EmployeeSalaryDataDto> employeeSalaryDataDtoList);
    EmployeeSalaryDataDto getEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month);
    boolean deleteEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month);

    List<EmployeeSalaryDataDto> getAllEmployeeSalaryDataForGivenEmployee(String pesel);

    boolean deleteAllEmployeeSalaryData(String pesel);
    void runCountMontlySalaryJob() throws SchedulerException, ParseException;
    void runCountAnnualSalaryJob() throws SchedulerException, ParseException;

}
