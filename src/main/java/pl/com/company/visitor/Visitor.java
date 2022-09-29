package pl.com.company.visitor;

import pl.com.company.model.EmployeeSalaryData;
import pl.com.company.repository.EmployeeRepo;
import pl.com.company.repository.EmployeeSalaryDataRepo;

import java.io.IOException;

public interface Visitor {
    void visit(Visitable visitable) throws IOException;
}
