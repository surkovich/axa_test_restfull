package jp.co.axa.apidemo.controller.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDTO {
  private Long id;
  private String name;
  private Integer salary;
  private String department;
}
