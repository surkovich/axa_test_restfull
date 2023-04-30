package jp.co.axa.apidemo.repository.impl;

import jp.co.axa.apidemo.core.api.Employee;
import jp.co.axa.apidemo.repository.impl.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface EmployeeEntityMapper {


  Employee toDomain(EmployeeEntity employeeEntity);

  EmployeeEntity toEntity(Employee employee);

}
