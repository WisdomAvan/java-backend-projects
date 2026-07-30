package africa.wisdom.taskTracker.controllers;

import africa.wisdom.taskTracker.data.enums.StatusType;
import africa.wisdom.taskTracker.data.models.Task;
import africa.wisdom.taskTracker.services.TaskService;

import java.util.List;

public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public Task addTask(String newDescription) {
        return taskService.addTask(newDescription);
    }

    public Task findTaskById(Long taskId) {
        return taskService.findTaskById(taskId);
    }

    public Task updateTask(Long taskId, String newDescription) {
       return taskService.updateTask(taskId, newDescription);
    }

    public void  deleteTask(Long taskId) {
        taskService.deleteTask(taskId);
    }

    public Task markTaskInProgress(Long taskId) {
        return taskService.markTaskInProgress(taskId);
    }

    public Task markTaskDone(Long taskId) {
        return taskService.markTaskDone(taskId);
    }

    public List<Task> findAllTasks() {
        return taskService.findAllTasks();
    }

    public List<Task> findTasksByStatus(StatusType status) {
        return taskService.findTasksByStatus(status);
    }

}
