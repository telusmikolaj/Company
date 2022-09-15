package pl.com.company.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class EmployeeRequestExceptionHandler {

    @ExceptionHandler(value = { EmployeeRequestException.class })
    public ResponseEntity<Object> handleEmployeeRequestException(EmployeeRequestException e) {
        EmployeeException employeeException = new EmployeeException(e.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now());
        return new ResponseEntity<>(employeeException, employeeException.getHttpStatus());
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleEmployeeDtoException(MethodArgumentNotValidException e) {
        EmployeeException employeeException = new EmployeeException(e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(employeeException, employeeException.getHttpStatus());
    }
}
