package africa.wisdom.taskTracker.data.models;

import africa.wisdom.taskTracker.data.enums.StatusType;
import africa.wisdom.taskTracker.exceptions.InvalidTaskDescriptionException;
import africa.wisdom.taskTracker.exceptions.InvalidTaskStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class TaskTest {

    @Test
    @DisplayName("Should assign TODO status when a task is created")
    void shouldAssignTodoStatus_WhenTaskIsCreated() {
        long taskId = 1L;
        String taskDescription = "Learn Python";
        Task task = new Task(taskId, taskDescription);
        assertThat(task.getStatus()).isEqualTo(StatusType.TODO);
    }

    @Test
    @DisplayName("Should save task description when a task is created")
    void shouldSaveTaskDescription_WhenTaskIsCreated() {
        long taskId = 1L;
        String taskDescription = "Learn Python";
        Task task = new Task(taskId, taskDescription);

        assertThat(task.getDescription()).isEqualTo(taskDescription);
    }

    @Test
    @DisplayName("Should save task Id when a task is created")
    void shouldSaveTaskId_WhenTaskIsCreated() {

        long taskId = 1L;
        String taskDescription = "Learn Python";
        Task task = new Task(taskId, taskDescription);

        assertThat(task.getTaskId()).isEqualTo(taskId);
    }

    @Test
    @DisplayName("Should initialize createdAt when task is created")
    void shouldInitializeCreatedAt_WhenTaskIsCreated() {

        long taskId = 1L;
        String taskDescription = "Learn Python";
        Task task = new Task(taskId, taskDescription);

        assertThat(task.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should initialize updateAt when task is created")
    void shouldInitializeUpdatedAt_WhenTaskIsCreated() {

        long taskId = 1L;
        String taskDescription = "Learn Python";
        Task task = new Task(taskId, taskDescription);

        assertThat(task.getUpdatedAt()).isNotNull();

    }

    @Test
    @DisplayName("Should update task description when a new description is provided")
    void  shouldUpdateTaskDescription_WhenNewDescriptionIsProvided() {

        Task task = new Task(1L, "I am going home");
        String newTaskDescription = "2026 Goals";

        task.updateDescription(newTaskDescription);

        assertThat(task.getDescription()).isEqualTo(newTaskDescription);
    }

    @Test
    @DisplayName("Should updated when task description is changed")
    void shouldUpdateUpdated_WhenTaskDescriptionIsChanged() throws InterruptedException {

        Task task  = new Task(1L, "I am going home");
        LocalDateTime previousUpdatedAt = LocalDateTime.now();
        Thread.sleep(10);

        task.updateDescription("Going for a vacation");

        assertThat(task.getUpdatedAt()).isAfter(previousUpdatedAt);
    }

    @Test
    @DisplayName("Should throw InvalidTaskDescriptionException when description is blank")
    void shouldThrowInvalidTaskDescriptionException_WhenDescriptionIsBlank() {
        Task task = new  Task(1L, "I am going home");

        assertThatThrownBy(()->task.updateDescription(""))
                .isInstanceOf(InvalidTaskDescriptionException.class)
                .hasMessage("Task description should not be empty");
    }

    @Test
    @DisplayName("Should throw InvalidTaskDescription when description is null")
    void shouldThrowInvalidTaskDescriptionException_WhenDescriptionIsNull() {

        Task task = new  Task(1L, "I am going home");

        assertThatThrownBy(()->task.updateDescription(null))
                .isInstanceOf(InvalidTaskDescriptionException.class)
                .hasMessage("Description should not be null");

    }

    @Test
    @DisplayName("Should mark task as IN_PROGRESS when markInProgress is called")
    void shouldMarkTaskAsInProgress_WhenTaskIsMarkedInProgress() {

        Task task = new Task(1L, "I am going home");
        task.markInProgress();

        assertThat(task.getStatus()).isEqualTo(StatusType.IN_PROGRESS);
    }

    @Test
    @DisplayName("Should update updatedAt when task is marked as IN_PROGRESS")
    void shouldUpdateUpdatedAt_WhenTaskIsMarkedAsInProgress() throws InterruptedException {

        Task task = new Task(1L, "I am going home");
        LocalDateTime previousUpdatedAt = LocalDateTime.now();
        Thread.sleep(10);

        task.markInProgress();

        assertThat(task.getUpdatedAt()).isAfter(previousUpdatedAt);

    }

    @Test
    @DisplayName("Should mark task as DONE when markDone is called")
    void shouldMarkTaskAsDone_WhenTaskIsMarkedAsDone() {
        Task task = new Task(1L, "Learn Java");
        task.markInProgress();

        task.markDone();

        assertThat(task.getStatus()).isEqualTo(StatusType.DONE);

    }

    @Test
    @DisplayName("Should throw InvalidTaskStateException when marking a TODO task as DONE")
    void shouldThrowInvalidTaskStateException_WhenMarkingATODOTaskAsDone() {
        Task task = new Task(1L, "Learn Java");

        assertThatThrownBy(task::markDone)
                .isInstanceOf(InvalidTaskStateException.class)
                .hasMessage("Task must be IN_PROGRESS before it can be marked as DONE");
    }

}