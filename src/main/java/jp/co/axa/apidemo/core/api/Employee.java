package jp.co.axa.apidemo.core.api;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Employee {
  private Long id;
  private String name;

  private Integer salary;

  private String department;

}
