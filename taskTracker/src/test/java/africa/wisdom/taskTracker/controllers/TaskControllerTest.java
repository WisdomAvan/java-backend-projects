package africa.wisdom.taskTracker.controllers;

import africa.wisdom.taskTracker.data.models.Task;
import africa.wisdom.taskTracker.persistence.implementation.JsonTaskRepository;
import africa.wisdom.taskTracker.persistence.repositories.TaskRepository;
import africa.wisdom.taskTracker.services.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    @Test
    @DisplayName("Should add a task through the controller")
    void shouldAddTaskThroughTheController() {

        TaskRepository repository = new JsonTaskRepository();
        TaskService service = new TaskService(repository);
        TaskController controller = new TaskController(service);

        Task task = controller.addTask("learning process");

        assertThat(task.getDescription()).isEqualTo("learning process");
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Should update a task through the controller")
    void shouldUpdateTask(){
        TaskRepository repository = new JsonTaskRepository();
        TaskService service = new TaskService(repository);
        TaskController controller = new TaskController(service);

        Task createdTask = controller.addTask("learning process");

        Task foundTask = controller.findTaskById(createdTask.getTaskId());

        assertThat(foundTask.getDescription()).isEqualTo("learning process");

    }

    @Test
    @DisplayName("Should return all tasks through the controller")
    void shouldReturnAllTasks(){
        TaskRepository repository = new JsonTaskRepository();
        TaskService service = new TaskService(repository);
        TaskController controller = new TaskController(service);

        controller.addTask("learning process");
        controller.addTask("learning java");
        controller.addTask("learning python");

        List < Task > tasks = controller.findAllTasks();
        assertThat(tasks).hasSize(3);
    }

}