package com.hrm.cloud.bravo.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyRepository extends JpaRepository<MyEntity, Long> {
}
