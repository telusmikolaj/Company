package pl.com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.com.company.service.EmployeeServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

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
    public EmployeeDto create(@Valid @RequestBody EmployeeDto dto) {
        return this.employeeService.create(dto);
    }

    @PutMapping()
    public EmployeeDto update(@Valid @RequestBody EmployeeDto dto) {
        return this.employeeService.update(dto);
    }

    @DeleteMapping("/{pesel}")
    public boolean delete(@PathVariable String pesel) {
        return this.employeeService.delete(pesel);
    }

    @GetMapping(path = "/page")
    public Page<EmployeeDto> getAllEmployees(@RequestParam int page, @RequestParam int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.employeeService.getAllEmployees(pageRequest);
    }
}
