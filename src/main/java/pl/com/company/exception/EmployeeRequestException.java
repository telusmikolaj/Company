package pl.com.company.exception;

public class EmployeeRequestException extends RuntimeException {

    public EmployeeRequestException(String message) {
        super(message);
    }
}
