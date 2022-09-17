package pl.com.company.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.com.company.controller.EmployeeDto;
import pl.com.company.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {


    EmployeeDto employeeToDto(Employee employee);
    Employee dtoToEmployee(EmployeeDto employeeDto);
}
