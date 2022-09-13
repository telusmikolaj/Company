package pl.com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.com.company.service.EmployeeServiceImpl;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{pesel}")
    public EmployeeDto get(@PathVariable String pesel) {
        return this.employeeService.get(pesel);
    }

    @PostMapping()
    public EmployeeDto create(@RequestBody EmployeeDto dto) {
        return this.employeeService.create(dto);
    }

    @PutMapping()
    public EmployeeDto update(@RequestBody EmployeeDto dto) {
        return this.employeeService.update(dto);
    }

    @DeleteMapping("/{pesel}")
    public boolean delete(@PathVariable String pesel) {
        return this.employeeService.delete(pesel);
    }
}
