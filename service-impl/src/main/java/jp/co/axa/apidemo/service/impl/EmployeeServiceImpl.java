package jp.co.axa.apidemo.service.impl;

import java.util.List;
import java.util.Optional;
import jp.co.axa.apidemo.core.api.Employee;
import jp.co.axa.apidemo.repository.api.EmployeeRepository;
import jp.co.axa.apidemo.service.api.EmployeeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {



    private final EmployeeRepository repository;


    @Override
    public @NonNull List<Employee> allEmployees() {
        return repository.findAll();
    }

    @Override
    public @NonNull Optional<Employee> getEmployee(long employeeId) {
        return repository.getById(employeeId);
    }

    @Override
    public Employee createEmployee(Employee employee){
        return repository.create(employee);
    }

    @Override
    public void markAsDeleted(long employeeId){
        repository.delete(employeeId);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return repository.update(employee);
    }

}