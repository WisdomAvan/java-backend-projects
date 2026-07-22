package africa.wisdom.taskTracker.data.models;
import africa.wisdom.taskTracker.data.enums.StatusType;
import africa.wisdom.taskTracker.exceptions.InvalidTaskDescriptionException;
import africa.wisdom.taskTracker.exceptions.InvalidTaskStateException;

import java.time.LocalDateTime;

public class Task {

    private Long taskId;
    private String taskDescription;
    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Task(Long taskId, String taskDescription) {

        this.taskId = taskId;
        this.taskDescription = taskDescription;
        this.status = StatusType.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private void validateUpdatedTask(String newDescription) {
        if(newDescription == null){
            throw new InvalidTaskDescriptionException("Description should not be null");
        }

        if(newDescription.isEmpty()) {
            throw new InvalidTaskDescriptionException("Task description should not be empty");
        }

    }


    public void updateDescription(String newDescription) {

       validateUpdatedTask(newDescription);
        this.taskDescription = newDescription;
        this.updatedAt = LocalDateTime.now();
    }

    public void markInProgress() {

        this.status = StatusType.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void markDone() {
        if(status != StatusType.IN_PROGRESS) {
            throw new InvalidTaskStateException("Task must be IN_PROGRESS before it can be marked as DONE");
        }
        this.status = StatusType.DONE;
        this.updatedAt = LocalDateTime.now();
    }

    public StatusType getStatus() {
        return status;
    }

    public String  getDescription() {
        return taskDescription;
    }

    public Long getTaskId() {
        return taskId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
