package pl.com.company.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.com.company.controller.EmployeeSalaryDataDto;
import pl.com.company.model.EmployeeSalaryData;

@Mapper(componentModel = "spring")
public interface SalaryDataMapper {
    EmployeeSalaryDataDto salaryDataToDto(EmployeeSalaryData employeeSalaryData);
    EmployeeSalaryData dtoToSalaryData(EmployeeSalaryDataDto employeeSalaryDataDto);
}
