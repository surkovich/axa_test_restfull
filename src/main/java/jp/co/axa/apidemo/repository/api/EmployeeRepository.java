package jp.co.axa.apidemo.repository.api;

import java.util.List;
import java.util.Optional;
import jp.co.axa.apidemo.core.api.Employee;
import lombok.NonNull;

public interface EmployeeRepository {

  @NonNull
  Optional<Employee> getById(Long id);

  @NonNull List<Employee> findAll();

  @NonNull Employee create(Employee employee);

  void delete(long employeeId);

  @NonNull Employee update(@NonNull Employee employee);
}
