package pl.com.company.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class EmployeeRequestExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private final MessageSource messageSource;

    public EmployeeRequestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = { EmployeeRequestException.class })
    public ResponseEntity<Object> handleEmployeeRequestException(EmployeeRequestException e) {
        String errorMessage = messageSource.getMessage("employee.notFound", new Object[]{e.getMessage()}, Locale.getDefault());
        ErrorInfo employeeException = new ErrorInfo(errorMessage, HttpStatus.CONFLICT, LocalDateTime.now());
        log.error(errorMessage);
        return new ResponseEntity<>(employeeException, employeeException.getHttpStatus());
    }

    @ExceptionHandler(value = { EmployeeNotFoundException.class })
    public ResponseEntity<Object> handleEmployeeNotFound(EmployeeNotFoundException e) {
        String errorMessage = messageSource.getMessage("employee.notFound", new Object[]{e.getPesel()}, Locale.getDefault());
        ErrorInfo employeeException = new ErrorInfo(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
        log.error(errorMessage);
        return new ResponseEntity<>(employeeException, employeeException.getHttpStatus());
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleEmployeeDtoException(MethodArgumentNotValidException e) {
        ErrorInfo employeeException = new ErrorInfo(getFieldErrorsString(e), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        log.error(getFieldErrorsString(e));
        return new ResponseEntity<>(employeeException, employeeException.getHttpStatus());
    }

    @ExceptionHandler(value = { EmployeeSalaryDataNotFoundException.class })
    public ResponseEntity<Object> handleEmployeeSalaryDataNotFoundException(EmployeeSalaryDataNotFoundException e) {
        String errorMessage = messageSource.getMessage("salaryData.notFound", new Object[]{e.getMessage()}, Locale.getDefault());
        ErrorInfo salaryDataException = new ErrorInfo(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
        log.error(errorMessage);
        return new ResponseEntity<>(salaryDataException, salaryDataException.getHttpStatus());
    }

    @ExceptionHandler(value = {  EmployeeSalaryDataAlreadyExistsException.class })
    public ResponseEntity<Object> handleEmployeeSalaryDataAlreadyExistsException( EmployeeSalaryDataAlreadyExistsException e) {
        String errorMessage = messageSource.getMessage("salaryData.alreadyExists", new Object[]{e.getMessage()}, Locale.getDefault());
        ErrorInfo salaryDataException = new ErrorInfo(errorMessage, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        log.error(errorMessage);
        return new ResponseEntity<>(salaryDataException, salaryDataException.getHttpStatus());
    }

    @ExceptionHandler(value = { EmployeeAllSalaryDataNotFound.class })
    public ResponseEntity<Object> handleEmployeeAllSalaryDataNotFound(EmployeeAllSalaryDataNotFound e) {
        String errorMessage = messageSource.getMessage("salaryDataAll.notFound", new Object[]{e.getPesel()}, Locale.getDefault());
        ErrorInfo salaryDataException = new ErrorInfo(errorMessage, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        log.error(errorMessage);
        return new ResponseEntity<>(salaryDataException, salaryDataException.getHttpStatus());
    }

    @ExceptionHandler(value = { InvalidSalaryDataUpdateFormatException.class })
    public ResponseEntity<Object> handleInvalidSalaryDataUpdateFormatException(InvalidSalaryDataUpdateFormatException e) {
        String errorMessage = messageSource.getMessage("salaryDataUpdate.invalidFormat", new Object[]{}, Locale.getDefault());
        ErrorInfo salaryDataException = new ErrorInfo(errorMessage, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        log.error(errorMessage);
        return new ResponseEntity<>(salaryDataException, salaryDataException.getHttpStatus());
    }

    @ExceptionHandler(value = { EmployeeAlreadyExistsException.class })
    public ResponseEntity<Object> handleEmployeeAlreadyExistsException(EmployeeAlreadyExistsException e) {
        String errorMessage = messageSource.getMessage("employee.alreadyExists", new Object[]{e.getPesel()}, Locale.getDefault());
        ErrorInfo salaryDataException = new ErrorInfo(errorMessage, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        log.error(errorMessage);
        return new ResponseEntity<>(salaryDataException, salaryDataException.getHttpStatus());
    }



    public String getFieldErrorsString(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> errors = e.getFieldErrors();
        errors.forEach(fieldError -> sb.append(fieldError.getDefaultMessage()));

        return sb.toString();
     }

}
