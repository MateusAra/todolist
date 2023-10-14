package br.com.mateusaraujo.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mateusaraujo.todolist.util.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    public TaskController(ITaskRepository taskRepository) {
        taskRepository = this.taskRepository;
    }

    @PostMapping("/")
    public ResponseEntity createTask(@RequestBody TaskModel task, HttpServletRequest request){
        
        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Data de inicio/ Data de termino deve ser maior que a atual");
        }

        if(task.getStartAt().isAfter(task.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Data de inicio não pode ser maior que a Data de termino");
        }

        task.setIdUser((Long)request.getAttribute("idUser"));
        TaskModel newTask = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newTask);
    }

    @GetMapping("/")
    public ResponseEntity GetAllTasks(HttpServletRequest request)
    {
        var idUser = request.getAttribute("idUser");
        List<TaskModel> tasks = taskRepository.findByIdUser((Long)idUser);

        if(tasks != null && tasks.size() > 0)
        {
            return ResponseEntity.status(HttpStatus.OK)
                .body(tasks);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Não existem tarefas cadastradas para o usuario");
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTasks(@RequestBody TaskModel task, HttpServletRequest request, @PathVariable Long id)
    {
        TaskModel taskExists = taskRepository.findById(id)
            .orElse(null);
        
        if(taskExists == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Tarefa não encontrada.");

        }
        
        var idUser = request.getAttribute("idUser");

        if(!taskExists.getIdUser().equals((Long)idUser))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Usuário não tem permissão pra alterar essa tarefa");

        }
        Utils.copyNonNullPropertys(task, taskExists);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(taskRepository.save(taskExists));
    }
}
