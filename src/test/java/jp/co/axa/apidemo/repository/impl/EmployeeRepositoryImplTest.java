package jp.co.axa.apidemo.repository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import jp.co.axa.apidemo.core.api.Employee;
import jp.co.axa.apidemo.core.api.exception.EmployeeNotExistsException;
import jp.co.axa.apidemo.repository.api.EmployeeRepository;
import jp.co.axa.apidemo.repository.impl.entity.EmployeeEntity;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class EmployeeRepositoryImplTest {

  private final EmployeeCrudRepository crud = mock(EmployeeCrudRepository.class);
  private final EmployeeEntityMapper mapper = Mappers.getMapper(EmployeeEntityMapper.class);;
  private final EmployeeRepository repository = new EmployeeRepositoryImpl(crud, mapper);

  public static final Employee EMPLOYEE_NULL_ID = new Employee(
      null,
      "some name",
      10,
      "department"
  );

  public static final EmployeeEntity ENTITY_NULL_ID = new EmployeeEntity(
      null,
      "some name",
      10,
      "department"
  );

  public static final EmployeeEntity ENTITY = new EmployeeEntity(
      1L,
      "some name",
      10,
      "department"
  );

  public static final Employee EMPLOYEE = new Employee(
      1L,
      "some name",
      10,
      "department"
  );

  @After
  public void verifyNoUselessInteractions(){
    verifyNoMoreInteractions(crud);
  }

  @Test
  public void WHEN_new_employee_is_created_THEN_crud_repository_should_be_called() {
    when(crud.save(any()))
        .thenReturn(ENTITY);

    Employee savedEmployee = repository.create(EMPLOYEE_NULL_ID);
    ArgumentCaptor<EmployeeEntity> capturedEntity = ArgumentCaptor.forClass(EmployeeEntity.class);
    assertEquals(EMPLOYEE, savedEmployee);
    verify(crud, times(1))
        .save(capturedEntity.capture());

    assertEquals(ENTITY_NULL_ID.toString(), capturedEntity.getValue().toString());
  }

  @Test
  public void WHEN_new_employee_with_id_is_attempted_to_be_created_THEN_no_id_should_be_passed_to_crud() {
    when(crud.save(any()))
        .thenReturn(ENTITY);

    Employee savedEmployee = repository.create(EMPLOYEE);
    ArgumentCaptor<EmployeeEntity> capturedEntity = ArgumentCaptor.forClass(EmployeeEntity.class);
    assertEquals(EMPLOYEE, savedEmployee);
    verify(crud, times(1))
        .save(capturedEntity.capture());

    assertEquals(ENTITY_NULL_ID.toString(), capturedEntity.getValue().toString());
  }

  @Test
  public void WHEN_crud_find_by_id_return_nothing_empty_optional_should_be_returned() {
    when(crud.findById(anyLong()))
        .thenReturn(Optional.empty());

    long requestedId = 1L;
    Optional<Employee> result = repository.getById(requestedId);

    verify(crud, times(1)).findById(eq(1L));

    assertFalse(result.isPresent());
  }

  @Test
  public void WHEN_crud_find_by_id_return_value_then_id_should_be_passed_as_result() {
    when(crud.findById(anyLong()))
        .thenReturn(Optional.of(ENTITY));

    long requestedId = 1L;
    Optional<Employee> result = repository.getById(requestedId);

    verify(crud, times(1)).findById(eq(1L));
    assertTrue(result.isPresent());
    assertEquals(EMPLOYEE.toString(), result.get().toString());
  }

  @Test
  public void WHEN_crud_returns_no_employees_on_findAll_THEN_empty_list_should_be_returned() {
    when(crud.findAll())
        .thenReturn(Collections.emptyList());

    assertEquals(0, repository.findAll().size());
    verify(crud, times(1)).findAll();
  }

  @Test
  public void WHEN_crud_returns_a_list_on_findAll_then_according_result_should_be_returned() {
    List<EmployeeEntity> resultFromCRUD = Collections.singletonList(
        ENTITY
    );
    when(crud.findAll())
        .thenReturn(resultFromCRUD);

    List<Employee> receivedResult = repository.findAll();
    assertEquals(1, receivedResult.size());

    assertEquals(mapper.toDomain(resultFromCRUD.get(0)).toString(), EMPLOYEE.toString());

    verify(crud, times(1)).findAll();
  }

  @Test
  public void WHEN_trying_to_delete_by_id_then_according_crud_method_should_be_called() {
    Long requestedId = 1L;
    doNothing().when(crud).deleteById(eq(requestedId));

    repository.delete(requestedId);
    verify(crud, times(1)).deleteById(eq(requestedId));
  }

  @Test
  public void WHEN_trying_to_update_non_existing_employee_THEN_EmployeeNotExistsException_should_be_thrown() {
    when(crud.getOne(eq(ENTITY.getId())))
        .thenThrow(EntityNotFoundException.class);

    try {
      repository.update(EMPLOYEE);
      throw new RuntimeException("Test failed, we shouldn't be here...");
    } catch (EmployeeNotExistsException e) {
      //do nothing, everythig is fine
    }
    verify(crud, times(1))
        .getOne(eq(EMPLOYEE.getId()));
  }

  @Test
  public void WHEN_trying_to_update_existing_entity_THEN_crud_save_should_be_called() {
    when(crud.getOne(eq(ENTITY.getId())))
        .thenReturn(ENTITY);
    when(crud.save(any()))
        .thenReturn(ENTITY);

    Employee savedEmployee = repository.update(EMPLOYEE);
    ArgumentCaptor<EmployeeEntity> capturedEntity = ArgumentCaptor.forClass(EmployeeEntity.class);

    verify(crud, times(1))
        .getOne(eq(EMPLOYEE.getId()));

    verify(crud, times(1))
        .save(capturedEntity.capture());

    assertEquals(EMPLOYEE, savedEmployee);

    assertEquals(ENTITY.toString(), capturedEntity.getValue().toString());
  }

}