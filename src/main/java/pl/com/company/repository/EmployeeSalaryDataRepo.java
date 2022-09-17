package pl.com.company.repository;

import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.model.Entity;
import pl.com.company.visitor.Visitable;
import pl.com.company.visitor.Visitor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface EmployeeSalaryDataRepo extends Visitable<EmployeeSalaryData>, Repository<EmployeeSalaryData>  {


    EmployeeSalaryData update(EmployeeSalaryData outdatedSalaryData, EmployeeSalaryData updatedSalaryData);

    EmployeeSalaryData getEmployeeSalaryForGivenMonthAndYear(String pesel, int year, int month);

    boolean deleteEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month);

    boolean isSalaryDataExsists(String pesel, int year, int month);



}

