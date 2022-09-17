package pl.com.company.service;

import org.springframework.stereotype.Service;
import pl.com.company.controller.EmployeeSalaryDataDto;
import pl.com.company.exception.*;
import pl.com.company.mapper.SalaryDataMapper;
import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeSalaryDataServiceImpl implements EmployeeSalaryDataService {

    private final EmployeeSalaryDataRepo employeeSalaryDataRepo;

    private final EmployeeRepo employeeRepo;

    private final SalaryDataMapper mapper;

    public EmployeeSalaryDataServiceImpl(EmployeeSalaryDataRepo employeeSalaryDataRepo, EmployeeRepo employeeRepo, SalaryDataMapper mapper) {
        this.employeeSalaryDataRepo = employeeSalaryDataRepo;
        this.employeeRepo = employeeRepo;
        this.mapper = mapper;
    }

    @Override
    public EmployeeSalaryDataDto create(EmployeeSalaryDataDto salaryDataDto) {

        if (this.employeeRepo.isEmployeeExists(salaryDataDto.getPesel())) {
            if (!this.employeeSalaryDataRepo.isSalaryDataExsists(salaryDataDto.getPesel(), salaryDataDto.getYear(), salaryDataDto.getMonth())) {

                EmployeeSalaryData employeeSalaryData =
                        this.employeeSalaryDataRepo.create(mapper.dtoToSalaryData(salaryDataDto));

                return mapper.salaryDataToDto(employeeSalaryData);
            }
            throw new EmployeeSalaryDataAlreadyExistsException(salaryDataDto.getPesel(), salaryDataDto.getMonth(), salaryDataDto.getYear());
        }
        throw new EmployeeNotFoundException(salaryDataDto.getPesel());
    }

    @Override
    public EmployeeSalaryDataDto update(List<EmployeeSalaryDataDto> salaryDataDtoList) {
        if (salaryDataDtoList.size() == 2) {


            EmployeeSalaryDataDto outdatedSalaryData = salaryDataDtoList.get(0);
            EmployeeSalaryDataDto updatedSalaryData = salaryDataDtoList.get(1);

            if (this.employeeRepo.isEmployeeExists(updatedSalaryData.getPesel())) {
                return mapper.salaryDataToDto(this.employeeSalaryDataRepo.update(mapper.dtoToSalaryData(outdatedSalaryData), mapper.dtoToSalaryData(updatedSalaryData)));
            }
            throw new EmployeeNotFoundException(outdatedSalaryData.getPesel());
        }

            throw new InvalidSalaryDataUpdateFormatException();
    }

    @Override
    public EmployeeSalaryDataDto getEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month) {

        if (this.employeeRepo.isEmployeeExists(pesel)) {
            if (this.employeeSalaryDataRepo.isSalaryDataExsists(pesel, year, month)) {
                return mapper.salaryDataToDto(this.employeeSalaryDataRepo.getEmployeeSalaryForGivenMonthAndYear(pesel, year, month));
            }
            throw new EmployeeSalaryDataNotFoundException(pesel, month, year);
        }
        throw new EmployeeNotFoundException(pesel);
    }

    @Override
    public List<EmployeeSalaryDataDto> getAllEmployeeSalaryDataForGivenEmployee(String pesel) {

        if (this.employeeRepo.isEmployeeExists(pesel)) {
            return this.employeeSalaryDataRepo.get(pesel).stream()
                    .map(mapper::salaryDataToDto)
                    .collect(Collectors.toList());
        }
        throw new EmployeeNotFoundException(pesel);
    }

    @Override
    public boolean deleteEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month){

        if (this.employeeRepo.isEmployeeExists(pesel)) {
            if (this.employeeSalaryDataRepo.isSalaryDataExsists(pesel, year, month)) {
                return this.employeeSalaryDataRepo.deleteEmployeeSalaryDataForGivenMonthAndYear(pesel, year, month);

            }
            throw new EmployeeSalaryDataNotFoundException(pesel, year, month);
        }
        throw new EmployeeNotFoundException(pesel);
    }

    @Override
    public boolean deleteAllEmployeeSalaryData(String pesel) {

        if(this.employeeRepo.isEmployeeExists(pesel)) {
            return this.employeeSalaryDataRepo.delete(pesel);
        }
        throw new EmployeeNotFoundException(pesel);
    }
}
