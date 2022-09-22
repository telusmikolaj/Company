package pl.com.company.repository;

import org.springframework.stereotype.Repository;
import pl.com.company.exception.EmployeeAllSalaryDataNotFound;
import pl.com.company.exception.EmployeeSalaryDataAlreadyExistsException;
import pl.com.company.exception.EmployeeSalaryDataNotFoundException;
import pl.com.company.exception.EmployeeSalaryDataRequestException;
import pl.com.company.model.Employee;
import pl.com.company.model.EmployeeSalaryData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class EmployeeSalaryDataIRepoImpl implements EmployeeSalaryDataRepo {

    private List<EmployeeSalaryData> employeeSalaryDataList;
    @Override
    public EmployeeSalaryData create(String pesel, int month, int year, BigDecimal monthSalary) {

        EmployeeSalaryData employeeSalaryData = new EmployeeSalaryData(pesel, month, year, monthSalary);

        if (isSalaryDataExsists(pesel, year, month)) {
            throw new EmployeeSalaryDataAlreadyExistsException(pesel, month, year);

        }

        employeeSalaryDataList.add(employeeSalaryData);

        return employeeSalaryData;
    }

    @Override
    public EmployeeSalaryData update(EmployeeSalaryData outdatedSalaryData, EmployeeSalaryData updatedSalaryData) {

        if (!isSalaryDataExsists
                (outdatedSalaryData.getPesel(),
                outdatedSalaryData.getYear(),
                outdatedSalaryData.getMonth())) {

            throw new EmployeeSalaryDataNotFoundException(outdatedSalaryData.getPesel(),
                    outdatedSalaryData.getMonth(), outdatedSalaryData.getYear());
        }

        if (isSalaryDataExsists(
                updatedSalaryData.getPesel(),
                updatedSalaryData.getYear(),
                updatedSalaryData.getMonth())) {

            throw new EmployeeSalaryDataAlreadyExistsException(updatedSalaryData.getPesel(),
                    updatedSalaryData.getMonth(), updatedSalaryData.getYear());
        }

        EmployeeSalaryData savedSalaryData = getEmployeeSalaryForGivenMonthAndYear(
                outdatedSalaryData.getPesel(), outdatedSalaryData.getYear(), outdatedSalaryData.getMonth());

        int index = this.employeeSalaryDataList.indexOf(savedSalaryData);

        this.employeeSalaryDataList.set(index, updatedSalaryData);

        return getEmployeeSalaryForGivenMonthAndYear(
                updatedSalaryData.getPesel(),
                updatedSalaryData.getYear(),
                updatedSalaryData.getMonth());
    }

    @Override
    public EmployeeSalaryData getEmployeeSalaryForGivenMonthAndYear(String pesel, int year, int month) {

        List <EmployeeSalaryData> filteredSalaryData = this.employeeSalaryDataList.stream()
                .filter(getPeselFilter(pesel).and(getMonthFilter(month).and(getYearFilter(year))))
                .collect(Collectors.toList());

            if (filteredSalaryData.size() == 0) {
                throw new EmployeeSalaryDataNotFoundException(pesel, month, year);
            }

            return filteredSalaryData.get(0);
    }

    @Override
    public List<EmployeeSalaryData> getAllEmployeeSalaryData(String pesel) {

        List <EmployeeSalaryData> filteredSalaryData = this.employeeSalaryDataList.stream()
                .filter(getPeselFilter(pesel))
                .collect(Collectors.toList());

        if (filteredSalaryData.size() == 0) {
            throw new EmployeeAllSalaryDataNotFound(pesel);

        }
        return filteredSalaryData;
    }

    @Override
    public boolean deleteAllEmployeeSalaryData(String pesel) {

        this.employeeSalaryDataList.removeIf(getPeselFilter(pesel));

        return true;
    }

    @Override
    public boolean deleteEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month) {

        boolean result = this.employeeSalaryDataList.removeIf(getPeselFilter(pesel)
                .and(getMonthFilter(month)
                        .and(getYearFilter(year))));

        if (result) {
            return true;
        }
            throw new EmployeeSalaryDataNotFoundException(pesel, month, year);
    }

    @Override
    public void deleteAll() {
        this.employeeSalaryDataList.clear();
    }

    @Override
    public boolean isSalaryDataExsists(String pesel, int year, int month) {
        List <EmployeeSalaryData> filteredSalaryData = this.employeeSalaryDataList.stream()
                .filter(getPeselFilter(pesel).and(getMonthFilter(month).and(getYearFilter(year))))
                .collect(Collectors.toList());

        return filteredSalaryData.size() != 0;
    }

    @Override
    public int size() {
        return this.employeeSalaryDataList.size();
    }

    private Predicate <EmployeeSalaryData> getPeselFilter(String pesel) {
        return salaryData -> salaryData.getPesel().equals(pesel);
    }

    private Predicate <EmployeeSalaryData> getMonthFilter(int month) {

        return salaryData -> salaryData.getMonth() == month;
    }

    private Predicate <EmployeeSalaryData> getYearFilter(int year) {

        return salaryData -> salaryData.getYear() == year;
    }

    public void loadAll(List<EmployeeSalaryData> savedList) {
        this.employeeSalaryDataList = savedList;
    }

    public List<EmployeeSalaryData> getAll() {
       return this.employeeSalaryDataList;
    }
}
