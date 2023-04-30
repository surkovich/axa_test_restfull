package jp.co.axa.apidemo.repository.impl.entity;

import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.ToString;

@Entity
@Table(name="EMPLOYEE")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeEntity {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "EMPLOYEE_PK_SEQ")
    @SequenceGenerator(name="EMPLOYEE_PK_SEQ", sequenceName="EMPLOYEE_PK_SEQ", allocationSize=1)
    @Column(name = "ID")
    private Long id;

    @Getter
    @Setter
    @Column(name="EMPLOYEE_NAME")
    private String name;

    @Getter
    @Setter
    @Column(name="EMPLOYEE_SALARY")
    private Integer salary;

    @Getter
    @Setter
    @Column(name="DEPARTMENT")
    private String department;

}
