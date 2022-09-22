package pl.com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.company.controller.EmployeeSalaryDataDto;
import pl.com.company.exception.EmployeeNotFoundException;
import pl.com.company.exception.EmployeeRequestException;
import pl.com.company.exception.InvalidSalaryDataUpdateFormatException;
import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeRepositoryDefault;
import pl.com.company.repository.EmployeeSalaryDataRepo;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeSalaryDataServiceImpl implements EmployeeSalaryDataService {

    private final EmployeeSalaryDataRepo employeeSalaryDataRepo;

    private final EmployeeRepo employeeRepo;

    public EmployeeSalaryDataServiceImpl(EmployeeSalaryDataRepo employeeSalaryDataRepo, EmployeeRepo employeeRepo) {
        this.employeeSalaryDataRepo = employeeSalaryDataRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public EmployeeSalaryDataDto create(EmployeeSalaryDataDto salaryDataDto) {

        if (this.employeeRepo.checkIfEmployeeExists(salaryDataDto.getPesel())) {
            EmployeeSalaryData employeeSalaryData = this.employeeSalaryDataRepo.create(
                    salaryDataDto.getPesel(),
                    salaryDataDto.getMonth(),
                    salaryDataDto.getYear(),
                    salaryDataDto.getMonthSalary()
            );
            return convertToDto(employeeSalaryData);
        }
        throw new EmployeeNotFoundException(salaryDataDto.getPesel());
    }

    @Override
    public EmployeeSalaryDataDto update(List<EmployeeSalaryDataDto> salaryDataDtoList) {
        if (salaryDataDtoList.size() == 2) {

            EmployeeSalaryDataDto outdatedSalaryData = salaryDataDtoList.get(0);
            EmployeeSalaryDataDto updatedSalaryData = salaryDataDtoList.get(1);

            if (this.employeeRepo.checkIfEmployeeExists(outdatedSalaryData.getPesel())) {
                return convertToDto(this.employeeSalaryDataRepo.update(convertToSalaryData(outdatedSalaryData), convertToSalaryData(updatedSalaryData)));
            }
            throw new EmployeeNotFoundException(outdatedSalaryData.getPesel());
        }

            throw new InvalidSalaryDataUpdateFormatException();
    }

    @Override
    public EmployeeSalaryDataDto getEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month) {

        if (this.employeeRepo.checkIfEmployeeExists(pesel)) {
            return convertToDto(this.employeeSalaryDataRepo.getEmployeeSalaryForGivenMonthAndYear(pesel, year, month));
        }
        throw new EmployeeNotFoundException(pesel);
    }

    @Override
    public List<EmployeeSalaryDataDto> getAllEmployeeSalaryDataForGivenEmployee(String pesel) {

        if (this.employeeRepo.checkIfEmployeeExists(pesel)) {
            return this.employeeSalaryDataRepo.getAllEmployeeSalaryData(pesel).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
        throw new EmployeeNotFoundException(pesel);
    }

    @Override
    public boolean deleteEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month){

        if (this.employeeRepo.checkIfEmployeeExists(pesel)) {
            return this.employeeSalaryDataRepo.deleteEmployeeSalaryDataForGivenMonthAndYear(pesel, year, month);

        }
        throw new EmployeeNotFoundException(pesel);
    }

    @Override
    public boolean deleteAllEmployeeSalaryData(String pesel) {

        if(this.employeeRepo.checkIfEmployeeExists(pesel)) {
            return this.employeeSalaryDataRepo.deleteAllEmployeeSalaryData(pesel);
        }
        throw new EmployeeNotFoundException(pesel);
    }

    public EmployeeSalaryDataDto convertToDto(EmployeeSalaryData employeeSalaryData) {
        EmployeeSalaryDataDto dto = new EmployeeSalaryDataDto();

        dto.setPesel(employeeSalaryData.getPesel());
        dto.setMonthSalary(employeeSalaryData.getMonthSalary());
        dto.setMonth(employeeSalaryData.getMonth());
        dto.setYear(employeeSalaryData.getYear());

        return dto;
    }

    public EmployeeSalaryData convertToSalaryData(EmployeeSalaryDataDto dto) {
        EmployeeSalaryData employeeSalaryData = new EmployeeSalaryData();
        employeeSalaryData.setPesel(dto.getPesel());
        employeeSalaryData.setMonth(dto.getMonth());
        employeeSalaryData.setYear(dto.getYear());
        employeeSalaryData.setMonthSalary(dto.getMonthSalary());

        return employeeSalaryData;
    }



}
