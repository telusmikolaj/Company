package pl.com.company.exception;

public class EmployeeAlreadyExistsException extends RuntimeException {

    private String pesel;

    public EmployeeAlreadyExistsException(String pesel) {
        this.pesel = pesel;
    }

    public String getPesel() {
        return pesel;
    }
}
