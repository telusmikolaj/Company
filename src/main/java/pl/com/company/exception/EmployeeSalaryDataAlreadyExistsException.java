package pl.com.company.exception;

public class EmployeeSalaryDataAlreadyExistsException extends RuntimeException {

    private String message;

    public  EmployeeSalaryDataAlreadyExistsException(String pesel, int month, int year) {
       StringBuilder sb = new StringBuilder();
       this.message = sb.append(pesel).append(" ").append(month).append("-").append(year).toString();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
