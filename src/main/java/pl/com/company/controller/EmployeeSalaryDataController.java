package pl.com.company.controller;

import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;
import pl.com.company.service.EmployeeSalaryDataService;

import javax.validation.Valid;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/salary")
public class EmployeeSalaryDataController {

    private final EmployeeSalaryDataService employeeSalaryDataService;
    public EmployeeSalaryDataController(EmployeeSalaryDataService employeeSalaryDataService) {
        this.employeeSalaryDataService = employeeSalaryDataService;
    }

    @GetMapping("/{pesel}/{year}/{month}")
    public EmployeeSalaryDataDto getEmployeeSalaryDataForGivenMonthAndYear(
            @PathVariable String pesel,
            @PathVariable int year,
            @PathVariable int month) {

        return this.employeeSalaryDataService.getEmployeeSalaryDataForGivenMonthAndYear(pesel, year, month);
    }

    @GetMapping("/{pesel}/all")
    public List <EmployeeSalaryDataDto> getAllSalaryDataForGivenEmployee(@PathVariable String pesel) {
        return this.employeeSalaryDataService.getAllEmployeeSalaryDataForGivenEmployee(pesel);
    }
    @DeleteMapping ("/{pesel}/{year}/{month}")
    public boolean deleteEmployeeSalaryDataFormSpecificMonthAndYear(
            @PathVariable String pesel,
            @PathVariable int year,
            @PathVariable int month) {

        return this.employeeSalaryDataService.deleteEmployeeSalaryDataForGivenMonthAndYear(pesel, year, month);
    }

    @DeleteMapping ("/{pesel}")
    public boolean deleteAllEmployeeSalaryData(@PathVariable String pesel)  {
        return this.employeeSalaryDataService.deleteAllEmployeeSalaryData(pesel);
    }

    @PostMapping()
    public EmployeeSalaryDataDto create(@Valid @RequestBody EmployeeSalaryDataDto employeeSalaryDto) {
        return this.employeeSalaryDataService.create(employeeSalaryDto);
    }

    @PutMapping()
    public EmployeeSalaryDataDto update(@Valid @RequestBody List<EmployeeSalaryDataDto> salaryDataDtoList) {

        return this.employeeSalaryDataService.update(salaryDataDtoList);
    }

    @PostMapping("/runmonthlyjob")
    public void runCountMontlySalaryJob() throws SchedulerException, ParseException {
        this.employeeSalaryDataService.runCountMontlySalaryJob();
    }
    @PostMapping("/runannualjob")
    public void runCountAnnualSalaryJob() throws SchedulerException, ParseException {
        this.employeeSalaryDataService.runCountAnnualSalaryJob();
    }

}
