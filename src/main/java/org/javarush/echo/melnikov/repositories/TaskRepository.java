package org.javarush.echo.melnikov.repositories;

import org.javarush.echo.melnikov.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nikolay Melnikov
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Page<Task> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);
}
