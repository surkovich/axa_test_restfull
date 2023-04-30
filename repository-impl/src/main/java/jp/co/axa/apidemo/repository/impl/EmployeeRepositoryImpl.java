package jp.co.axa.apidemo.repository.impl;

import jp.co.axa.apidemo.repository.impl.entity.EmployeeEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import jp.co.axa.apidemo.core.api.Employee;
import jp.co.axa.apidemo.core.api.exception.EmployeeNotExistsException;
import jp.co.axa.apidemo.repository.api.EmployeeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

  private final EmployeeCrudRepository crud;

  private final EmployeeEntityMapper mapper;

  @Override
  public @NonNull Optional<Employee> getById(Long id) {
    return crud.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public @NonNull List<Employee> findAll() {
    return crud.findAll()
        .stream().map(
            mapper::toDomain
        ).collect(Collectors.toList());
  }

  @NonNull
  @Override
  public Employee create(Employee employee) {
    EmployeeEntity entity = mapper.toEntity(employee);
    entity.setId(null);
    return mapper.toDomain(crud.save(entity));
  }

  @Override
  public void delete(long employeeId) {
    //TODO change to markAsInactive and\or move to audit?
    crud.deleteById(employeeId);
  }

  /**
   * Throws javax.persistence.EntityNotFoundException when entity with this ID doesn't exist
   */
  @NonNull
  @Override
  public Employee update(@NonNull Employee employee) {
    try {
      EmployeeEntity existing = crud.getOne(employee.getId());

      EmployeeEntity newData = mapper.toEntity(employee);
      existing.setName(newData.getName());
      existing.setDepartment(newData.getDepartment());
      existing.setSalary(newData.getSalary());

      return mapper.toDomain(crud.save(existing));
    } catch (EntityNotFoundException ex) {
      throw new EmployeeNotExistsException();
    }
  }

}
