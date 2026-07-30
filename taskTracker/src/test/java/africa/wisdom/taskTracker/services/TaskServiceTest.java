package africa.wisdom.taskTracker.services;

import africa.wisdom.taskTracker.controllers.TaskController;
import africa.wisdom.taskTracker.data.enums.StatusType;
import africa.wisdom.taskTracker.data.models.Task;
import africa.wisdom.taskTracker.persistence.implementation.JsonTaskRepository;
import africa.wisdom.taskTracker.persistence.repositories.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskServiceTest {

    @Test
    @DisplayName("Should create and save a new task")
    void shouldCreateAndSaveTask() {

        TaskRepository repository = new JsonTaskRepository();
        TaskService taskService = new TaskService(repository);


        Task createdTask = taskService.addTask("Learning took place");


        assertThat(createdTask.getDescription())
                .isEqualTo("Learning took place");

        assertThat(repository.findAll())
                .hasSize(1);
    }

    @Test
    @DisplayName("Should update an existing task description")
    void shouldUpdateTask() {
        TaskRepository repository = new JsonTaskRepository();
        TaskService taskService = new TaskService(repository);

        Task createdTask = taskService.addTask("Learning took place");

        Task updatedTask = taskService.updateTask(createdTask.getTaskId(), "Learn");

        assertThat(updatedTask.getDescription())
                .isEqualTo("Learn");
        assertThat(repository.findById(createdTask.getTaskId())).isPresent();
        assertThat(repository.findById(createdTask.getTaskId()).get().getDescription())
                .isEqualTo("Learn");
    }

    @Test
    @DisplayName("Should delete an existing task")
    void shouldDeleteTask_WhenTaskExists() {


        TaskRepository repository = new JsonTaskRepository();
        TaskService taskService = new TaskService(repository);

        Task task = taskService.addTask("Learning took place");

        taskService.deleteTask(task.getTaskId());

        assertThat(repository.findAll()).isEmpty();
        assertThat(repository.findById(task.getTaskId())).isEmpty();
    }

    @Test
    @DisplayName("Should find a task by its Id")
    void shouldFindTaskById() {
        TaskRepository repository = new JsonTaskRepository();
        TaskService taskService = new TaskService(repository);

        Task createdTask = taskService.addTask("Learning took place");
        Task foundTask = taskService.findTaskById(createdTask.getTaskId());

        assertThat(foundTask).isEqualTo(createdTask);
    }
    @Test
    @DisplayName("Should return all task")
    void shouldAllTask() {
        TaskRepository repository = new JsonTaskRepository();
        TaskService taskService = new TaskService(repository);

        Task firstTask = taskService.addTask("Learning took place");
        Task secondTask = taskService.addTask("Learning place");
        Task thirdTask = taskService.addTask("Learning");

        List<Task> tasks = taskService.findAllTasks();

        assertThat(tasks).hasSize(3)
                .containsExactly(firstTask, secondTask, thirdTask);
    }

    @Test
    @DisplayName("Should return all tasks with DONE status")
    void shouldReturnAllDoneTasks() {

        TaskRepository repository = new JsonTaskRepository();
        TaskService taskService = new TaskService(repository);

        Task firstTask = taskService.addTask("Learn Java");
        Task secondTask = taskService.addTask("Learn Spring");
        Task thirdTask = taskService.addTask("Write Tests");
        Task fourthTask = taskService.addTask("Deploy App");

        taskService.markTaskInProgress(secondTask.getTaskId());
        taskService.markTaskDone(secondTask.getTaskId());

        taskService.markTaskInProgress(thirdTask.getTaskId());
        taskService.markTaskDone(thirdTask.getTaskId());

        List<Task> doneTasks = taskService.findTasksByStatus(StatusType.DONE);

        assertThat(doneTasks).hasSize(2);

        assertThat(doneTasks)
                .containsExactly(secondTask, thirdTask);
    }
}