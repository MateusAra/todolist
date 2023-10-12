package br.com.mateusaraujo.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;
    public TaskController(ITaskRepository taskRepository)
    {
        taskRepository = this.taskRepository;
    }

    @PostMapping("/createTask")
    public ResponseEntity createTask(@RequestBody TaskModel task){

        TaskModel newTask = taskRepository.save(task);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newTask);
    }
}
