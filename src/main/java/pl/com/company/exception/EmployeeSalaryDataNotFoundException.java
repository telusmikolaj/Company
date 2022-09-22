package pl.com.company.exception;

import pl.com.company.model.EmployeeSalaryData;

public class EmployeeSalaryDataNotFoundException extends RuntimeException {

    private String message;
    public EmployeeSalaryDataNotFoundException(String pesel, int month, int year) {
        StringBuilder sb = new StringBuilder();
        this.message = sb.append(pesel).append(" ").append(month).append("-").append(year).toString();
    }

    @Override
    public String getMessage() {
        return message;
    }

}
