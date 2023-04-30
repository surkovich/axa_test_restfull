package jp.co.axa.apidemo.controller.impl;

import jp.co.axa.apidemo.controller.api.EmployeeDTO;
import jp.co.axa.apidemo.core.api.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface EmployeeDTOMapper {

  EmployeeDTOMapper INSTANCE = Mappers.getMapper(EmployeeDTOMapper.class);

  EmployeeDTO toDTO(Employee employee);

  Employee toDomain(EmployeeDTO employeeDTO);

  @Mapping(target = "id", ignore = true)
  Employee toDomainWithoutId(EmployeeDTO employeeDTO);

}
