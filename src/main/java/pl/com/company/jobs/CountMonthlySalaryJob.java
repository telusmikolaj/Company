package pl.com.company.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.repository.EmployeeSalaryDataRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

@Component
public class CountMonthlySalaryJob implements Job {

    @Autowired
    EmployeeSalaryDataRepo salaryDataRepo;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();
        List<EmployeeSalaryData> salaryDataList = salaryDataRepo.getAll();

        BigDecimal monthlySum = salaryDataList.stream()
                .filter(getMonthFilter(currentMonth).and(getYearFilter(currentYear)))
                .map(EmployeeSalaryData::getMonthSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        logger.info("Salary sum for " + currentMonth + "." + currentYear + ": " + monthlySum);
    }

    private Predicate<EmployeeSalaryData> getMonthFilter(int month) {
        return salaryData -> salaryData.getMonth() == month;
    }

    private Predicate<EmployeeSalaryData> getYearFilter(int year) {
        return salaryData -> salaryData.getYear() == year;
    }

}
