package africa.wisdom.taskTracker.persistence.repositories;

import africa.wisdom.taskTracker.data.models.Task;

import java.util.List;
import java.util.Optional;


public interface TaskRepository {
    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAll();

    void deleteById(Long id);


}
