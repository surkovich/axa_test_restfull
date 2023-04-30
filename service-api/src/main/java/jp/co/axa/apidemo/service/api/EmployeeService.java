package jp.co.axa.apidemo.service.api;

import java.util.Optional;
import jp.co.axa.apidemo.core.api.Employee;

import java.util.List;
import lombok.NonNull;

public interface EmployeeService {

    List<Employee> allEmployees();

    Optional<Employee> getEmployee(long employeeId);

    @NonNull Employee createEmployee(@NonNull Employee employee);

    void markAsDeleted(long employeeId);

    Employee updateEmployee(Employee employee);
}