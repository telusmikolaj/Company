package pl.com.company.exception;

public class EmployeeAllSalaryDataNotFound extends RuntimeException {
    private String pesel;

    public EmployeeAllSalaryDataNotFound(String pesel) {
        this.pesel = pesel;
    }

    public String getPesel() {
        return pesel;
    }
}
