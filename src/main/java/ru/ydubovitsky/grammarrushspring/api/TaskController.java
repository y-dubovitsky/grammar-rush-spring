package ru.ydubovitsky.grammarrushspring.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ydubovitsky.grammarrushspring.dto.TaskRequestDto;
import ru.ydubovitsky.grammarrushspring.dto.TaskResponseDto;
import ru.ydubovitsky.grammarrushspring.entity.Task;
import ru.ydubovitsky.grammarrushspring.facade.TaskFacade;
import ru.ydubovitsky.grammarrushspring.service.TaskService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(@Qualifier("TaskServiceDatabaseImpl") TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();
        if(allTasks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tasks not found");
        }
        List<TaskResponseDto> response = allTasks.stream()
                .map(task -> TaskFacade.taskToTaskResponseDto(task))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<TaskResponseDto> addNewTask(@RequestBody TaskRequestDto taskRequestDto) {
        Task savedTask = taskService.addNewTask(taskRequestDto);
        return ResponseEntity.ok(TaskFacade.taskToTaskResponseDto(savedTask));
    }

}
