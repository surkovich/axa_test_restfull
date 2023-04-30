package jp.co.axa.apidemo.repository.impl;

import jp.co.axa.apidemo.repository.impl.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeCrudRepository extends JpaRepository<EmployeeEntity,Long> {
}
