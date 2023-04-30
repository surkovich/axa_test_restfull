package jp.co.axa.apidemo.controller.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jp.co.axa.apidemo.controller.api.EmployeeDTO;
import jp.co.axa.apidemo.core.api.Employee;
import jp.co.axa.apidemo.core.api.exception.EmployeeNotExistsException;
import jp.co.axa.apidemo.service.api.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

  private final EmployeeService service;

  private final EmployeeDTOMapper mapper;

  @GetMapping("/employees")
  public List<EmployeeDTO> getAllEmployees() {
    return service
        .allEmployees()
        .stream().map(mapper::toDTO)
        .collect(Collectors.toList());
  }

  @GetMapping("/employees/{employeeId}")
  public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable(name = "employeeId") long employeeId) {
    Optional<EmployeeDTO> result = service.getEmployee(employeeId)
        .map(mapper::toDTO);
    return result
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping("/employees")
  public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
    Employee savedEmployee = service.createEmployee(mapper.toDomainWithoutId(employeeDTO));
    log.info("Employee by request [{}] Saved Successfully with result [{}] ", employeeDTO, savedEmployee);
    return mapper.toDTO(savedEmployee);
  }

  @DeleteMapping("/employees/{employeeId}")
  public void deleteEmployee(@PathVariable(name = "employeeId") long employeeId) {
    service.markAsDeleted(employeeId);
    log.info("Employee with id [{}] Deleted Successfully", employeeId);
  }

  @PutMapping("/employees/{employeeId}")
  public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employee,
      @PathVariable(name = "employeeId") Long employeeId) {
    if (employee == null) {
      return ResponseEntity.badRequest().build();
    }
    if (employee.getId() == null || !employeeId.equals(employee.getId())) {
      return ResponseEntity.badRequest().build();
    }
    try {
      return ResponseEntity.ok(
          mapper.toDTO(
              service.updateEmployee(mapper.toDomain(employee))
          )
      );
    } catch (EmployeeNotExistsException ex) {
      return ResponseEntity.badRequest().build();
    }
  }

}
