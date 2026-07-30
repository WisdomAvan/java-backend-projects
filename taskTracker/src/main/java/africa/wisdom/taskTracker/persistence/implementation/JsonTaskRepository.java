package africa.wisdom.taskTracker.persistence.implementation;

import africa.wisdom.taskTracker.data.models.Task;
import africa.wisdom.taskTracker.persistence.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class JsonTaskRepository implements TaskRepository {

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public Task save(Task task) {
        tasks.add(task);
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {

        for (Task task : tasks) {
            if (task.getTaskId().equals(id)) {
                return Optional.of(task);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        return tasks;
    }

    @Override
    public void deleteById(Long id) {

        Iterator<Task> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            Task task = iterator.next();

            if (task.getTaskId().equals(id)) {
                iterator.remove();
                return;
            }
        }
    }
}