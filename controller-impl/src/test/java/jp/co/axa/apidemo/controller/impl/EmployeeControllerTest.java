package jp.co.axa.apidemo.controller.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import jp.co.axa.apidemo.controller.api.EmployeeDTO;
import jp.co.axa.apidemo.core.api.Employee;
import jp.co.axa.apidemo.core.api.exception.EmployeeNotExistsException;
import jp.co.axa.apidemo.service.api.EmployeeService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {


  public static final Employee EMPLOYEE = new Employee(
      1L,
      "some name",
      10,
      "department"
  );

  public static final Employee NO_ID_EMPLOYEE = new Employee(
      null,
      "some name",
      10,
      "department"
  );

  public static final EmployeeDTO EMPLOYEE_DTO = new EmployeeDTO(
      1L,
      "some name",
      10,
      "department"
  );

  public static final EmployeeDTO NEW_EMPLOYEE_DTO = new EmployeeDTO(
      null,
      "some name",
      10,
      "department"
  );

  private final EmployeeService service = mock(EmployeeService.class);
  private final EmployeeDTOMapper dtoMapper = EmployeeDTOMapper.INSTANCE;

  private final EmployeeController controller = new EmployeeController(service, dtoMapper);

  @After
  public void verifyNoUselessInteractions() {
    verifyNoMoreInteractions(service);
  }

  @Test
  public void WHEN_no_employees_returned_then_getAllEmployees_should_return_empty_list() {
    when(service.allEmployees()).thenReturn(Collections.emptyList());
    List<EmployeeDTO> response = controller.getAllEmployees();
    assertEquals(0, response.size());
    verify(service, times(1)).allEmployees();
  }

  @Test
  public void WHEN_employee_returned_in_the_list_then_getAllEmployees_should_contain_accordingDTO() {
    when(service.allEmployees()).thenReturn(
        Collections.singletonList(
            EMPLOYEE
        )
    );

    List<EmployeeDTO> response = controller.getAllEmployees();
    assertEquals(1, response.size());

    verify(service, times(1)).allEmployees();
  }

  @Test
  public void WHEN_employee_returned_by_id_THEN_result_should_contain_according_DTO() {

    long requestedId = 1L;
    when(service.getEmployee(eq(requestedId)))
        .thenReturn(Optional.of(EMPLOYEE));

    ResponseEntity<EmployeeDTO> receivedResult = controller.getEmployee(requestedId);

    EmployeeDTO receivedDTO = receivedResult.getBody();
    assertEquals(EMPLOYEE_DTO, receivedDTO);
    assertEquals(HttpStatus.OK, receivedResult.getStatusCode());

    verify(service, times(1)).getEmployee(eq(requestedId));
  }

  @Test
  public void WHEN_service_returns_empty_response_THEN_not_found_status_should_be_returned() {
    long requestedId = 1L;
    when(service.getEmployee(eq(requestedId)))
        .thenReturn(Optional.empty());
    ResponseEntity<EmployeeDTO> receivedEntity = controller.getEmployee(requestedId);

    assertEquals(HttpStatus.NOT_FOUND, receivedEntity.getStatusCode());
    assertNull(receivedEntity.getBody());

    verify(service, times(1)).getEmployee(eq(requestedId));
  }

  @Test
  public void WHEN_new_employee_is_created_then_service_should_be_called() {

    when(service.createEmployee(any()))
        .thenReturn(EMPLOYEE);

    EmployeeDTO receivedDTO = controller.createEmployee(NEW_EMPLOYEE_DTO);

    assertEquals(EMPLOYEE_DTO, receivedDTO);

    verify(service, times(1)).createEmployee(eq(NO_ID_EMPLOYEE));
  }

  @Test
  public void WHEN_new_employee_with_id_is_created_then_no_id_should_pass_to_service() {
    when(service.createEmployee(any()))
        .thenReturn(EMPLOYEE);

    EmployeeDTO receivedDTO = controller.createEmployee(EMPLOYEE_DTO);

    assertEquals(EMPLOYEE_DTO, receivedDTO);

    verify(service, times(1)).createEmployee(eq(NO_ID_EMPLOYEE));
  }

  @Test
  public void WHEN_deleting_employee_by_id_then_service_should_be_called() {
    long requestedId = 1L;
    doNothing().when(service).markAsDeleted(eq(requestedId));

    controller.deleteEmployee(requestedId);
    verify(service, times(1)).markAsDeleted(eq(requestedId));
  }

  @Test
  public void WHEN_update_has_null_body_THEN_bad_request_status_should_be_returned() {
    ResponseEntity<EmployeeDTO> response = controller.updateEmployee(null, 1L);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void attempt_to_update_entity_to_another_id_should_return_bad_request() {
    ResponseEntity<EmployeeDTO> response = controller.updateEmployee(EMPLOYEE_DTO, 2L);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void updating_a_well_formed_employee_should_invoke_service() {
    when(service.updateEmployee(EMPLOYEE))
        .thenReturn(EMPLOYEE);

    ResponseEntity<EmployeeDTO> response = controller.updateEmployee(EMPLOYEE_DTO, EMPLOYEE_DTO.getId());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    assertEquals(EMPLOYEE_DTO, response.getBody());
    verify(service, times(1)).updateEmployee(eq(EMPLOYEE));
  }

  @Test
  public void WHEN_service_reports_about_not_existing_entity_THEN_bad_request_status_should_be_returned() {

    when(service.updateEmployee(EMPLOYEE))
        .thenThrow(new EmployeeNotExistsException());
    ResponseEntity<EmployeeDTO> response = controller.updateEmployee(EMPLOYEE_DTO, EMPLOYEE_DTO.getId());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    verify(service, times(1)).updateEmployee(eq(EMPLOYEE));
  }

}