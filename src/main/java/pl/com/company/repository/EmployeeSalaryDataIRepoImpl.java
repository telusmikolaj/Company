package pl.com.company.repository;

import org.springframework.stereotype.Repository;
import pl.com.company.exception.EmployeeSalaryDataAlreadyExistsException;
import pl.com.company.exception.EmployeeSalaryDataNotFoundException;
import pl.com.company.model.EmployeeSalaryData;

import java.util.List;
import java.util.function.Predicate;

@Repository
public class EmployeeSalaryDataIRepoImpl extends AbstractRepository<EmployeeSalaryData> implements EmployeeSalaryDataRepo {
    private List<EmployeeSalaryData> employeeSalaryDataList;

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

        this.loadAll(this.employeeSalaryDataList);

        return getEmployeeSalaryForGivenMonthAndYear(
                updatedSalaryData.getPesel(),
                updatedSalaryData.getYear(),
                updatedSalaryData.getMonth());
    }

    @Override
    public EmployeeSalaryData getEmployeeSalaryForGivenMonthAndYear(String pesel, int year, int month) {
        this.employeeSalaryDataList = this.getAll();
        EmployeeSalaryData employeeSalaryData = this.employeeSalaryDataList.stream().filter(getPeselFilter(pesel).and(getYearFilter(year).and(getMonthFilter(month))))
                .findAny()
                .orElseThrow(() -> new EmployeeSalaryDataNotFoundException(pesel, year, month));

        return employeeSalaryData;
    }

    @Override
    public boolean deleteEmployeeSalaryDataForGivenMonthAndYear(String pesel, int year, int month) {
            this.employeeSalaryDataList = this.getAll();
            boolean result = this.employeeSalaryDataList.removeIf(getPeselFilter(pesel)
                    .and(getMonthFilter(month)
                            .and(getYearFilter(year))));

            this.loadAll(this.employeeSalaryDataList);
            return result;
    }

    @Override
    public boolean isSalaryDataExsists(String pesel, int year, int month) {
        this.employeeSalaryDataList = this.getAll();
        return this.employeeSalaryDataList.stream()
                .anyMatch(getPeselFilter(pesel).and(getMonthFilter(month).and(getYearFilter(year))));

    }

    private Predicate<EmployeeSalaryData> getPeselFilter(String pesel) {
        return salaryData -> salaryData.getPesel().equals(pesel);
    }

    private Predicate<EmployeeSalaryData> getMonthFilter(int month) {

        return salaryData -> salaryData.getMonth() == month;
    }

    private Predicate<EmployeeSalaryData> getYearFilter(int year) {

        return salaryData -> salaryData.getYear() == year;
    }

}

