package africa.wisdom.taskTracker.persistence.implementation;

import africa.wisdom.taskTracker.data.models.Task;
import africa.wisdom.taskTracker.persistence.repositories.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;


class JsonTaskRepositoryTest {

    @Test
    @DisplayName("Should save a task")
    void shouldSaveTask() {
        JsonTaskRepository jsonTaskRepository = new JsonTaskRepository();
        Task task = new Task(1L, "Traveling");

        Task savedTask = jsonTaskRepository.save(task);

        assertThat(savedTask).isEqualTo(task);
    }

    @Test
    @DisplayName("Should find a saved task by Id")
    void  shouldFindTaskById_AfterSavingTask() {
        JsonTaskRepository jsonTaskRepository = new JsonTaskRepository();
        Task task = new Task(1L, "Traveling");
        jsonTaskRepository.save(task);

        Optional<Task> foundTask = jsonTaskRepository.findById(1L);

        assertThat(foundTask).isPresent();
        assertThat(foundTask.get()).isEqualTo(task);

    }

    @Test
    @DisplayName("Should return all saved tasks")
    void shouldReturnAllSavedTasks() {

        TaskRepository repository = new JsonTaskRepository();

        Task firstTask = new Task(1L, "Traveling");
        Task secondTask = new Task(2L, "Going to school");

        repository.save(firstTask);
        repository.save(secondTask);

        List<Task> savedTasks = repository.findAll();

        assertThat(savedTasks)
                .hasSize(2)
                .containsExactly(firstTask, secondTask);
    }

    @Test
    @DisplayName("Should delete a task by its id")
    void shouldDeleteTaskById_AfterDeletingTask() {
        JsonTaskRepository repository= new JsonTaskRepository();

        Task firstTask = new Task(1L, "Traveling");
        Task secondTask = new Task(2L, "Going to school");
        Task thirdTask = new Task(3L, "Building a house");
        repository.save(firstTask);
        repository.save(secondTask);
        repository.save(thirdTask);

        repository.deleteById(1L);

        assertThat(repository.findAll()).hasSize(2)
                .containsExactly(secondTask,thirdTask);
        assertThat(repository.findById(1L)).isEmpty();


    }
}