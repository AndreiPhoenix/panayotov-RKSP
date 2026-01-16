package ru.rksp.panayotov.processor.repository;

import ru.rksp.panayotov.processor.entity.PostgreSQLEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgreSQLRepository extends JpaRepository<PostgreSQLEvent, Long> {
}