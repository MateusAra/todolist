package br.com.mateusaraujo.todolist.task;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ITaskRepository extends JpaRepository<TaskModel, Long>{
    List<TaskModel> findByIdUser(Long idUser);
}
