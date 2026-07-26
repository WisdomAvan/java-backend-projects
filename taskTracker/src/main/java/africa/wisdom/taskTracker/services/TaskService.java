package africa.wisdom.taskTracker.services;

import africa.wisdom.taskTracker.data.models.Task;
import africa.wisdom.taskTracker.persistence.repositories.TaskRepository;

import java.util.List;

public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task addTask(String description) {

        Long taskId = (long) repository.findAll().size()+1;
        Task task = new Task(taskId, description);

        return repository.save(task);
    }

    private Task getTaskById(Long taskId) {
        return repository.findById(taskId).orElseThrow();
    }

    public Task updatedTask(Long Id, String newDescription) {

        Task task =  getTaskById(Id);
        task.updateDescription(newDescription);
        return task;
    }

    public void deleteTask(Long taskId) {
        getTaskById(taskId);
        repository.deleteById(taskId);
    }

    public Task findTaskById(Long taskId) {

        return getTaskById(taskId);

    }

    public List<Task> findAllTasks() {
        return repository.findAll();
    }
}