package org.javarush.echo.melnikov.services;

import org.javarush.echo.melnikov.domain.Task;
import org.javarush.echo.melnikov.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Nikolay Melnikov
 */
@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Optional<Task> findById(Integer id) {
        return taskRepository.findById(id);
    }

    public Page<Task> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable) {
        return taskRepository.findByDescriptionContainingIgnoreCase(keyword, pageable);
    }



    @Transactional
    public void update(Integer id, Task updatedTask) {
        updatedTask.setId(id);
        taskRepository.save(updatedTask);
    }

    @Transactional
    public void delete(Integer id) {
        taskRepository.deleteById(id);
    }

}
