package org.javarush.echo.melnikov.controllers;

import jakarta.validation.Valid;
import org.javarush.echo.melnikov.domain.Task;
import org.javarush.echo.melnikov.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * @author Nikolay Melnikov
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public String show(Model model,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false, defaultValue = "1") int page,
                       @RequestParam(required = false, defaultValue = "10") int size,
                       @RequestParam(required = false, defaultValue = "id,asc") Optional<String>[] sort) {
        try {
            List<Task> tasks;
            String sortField = sort[0].orElse("id");
            String sortDirection = sort[1].orElse("asc");

            Direction direction = sortDirection.equals("asc") ? Direction.ASC : Direction.DESC;
            Order order = new Order(direction, sortField);

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

            Page<Task> pageTasks;

            if (keyword == null) {
                pageTasks = taskService.findAll(pageable);
            } else {
                pageTasks = taskService.findByDescriptionContainingIgnoreCase(keyword, pageable);
                model.addAttribute("keyword", keyword);
            }

            tasks = pageTasks.getContent();

            model.addAttribute("tasks", tasks);
            model.addAttribute("currentPage", pageTasks.getNumber() + 1);
            model.addAttribute("totalPages", pageTasks.getTotalPages());
            model.addAttribute("totalItems", pageTasks.getTotalElements());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
            model.addAttribute("pageSizes", List.of(3, 9, 12));
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "tasks/index";
    }

    @GetMapping("/new")
    public String newTask(@ModelAttribute("task") Task task) {
        return "tasks/new";
    }

    @PostMapping()
    public String save(@ModelAttribute("task") @Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/new";
        }
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Task task = taskService.findById(id).orElse(null);
        return "tasks/edit";
    }

    @PatchMapping("/{id}")
    public String update (@PathVariable("id") Integer id,
                          @ModelAttribute("task") @Valid Task task,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/edit";
        }
        taskService.update(id, task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            taskService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Task id " + id + "deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/tasks";
    }
}
