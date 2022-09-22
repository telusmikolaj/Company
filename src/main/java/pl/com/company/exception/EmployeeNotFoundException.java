package pl.com.company.exception;

public class EmployeeNotFoundException  extends RuntimeException {

        private String pesel;

        public EmployeeNotFoundException(String pesel) {
                this.pesel = pesel;
        }

        public String getPesel() {
                return pesel;
        }
}
