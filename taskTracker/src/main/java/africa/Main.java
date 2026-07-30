package africa;

import africa.wisdom.taskTracker.controllers.TaskController;
import africa.wisdom.taskTracker.data.models.StatusType;
import africa.wisdom.taskTracker.data.models.Task;
import africa.wisdom.taskTracker.persistence.implementation.JsonTaskRepository;
import africa.wisdom.taskTracker.persistence.repositories.TaskRepository;
import africa.wisdom.taskTracker.services.TaskService;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskRepository repository = new JsonTaskRepository();
        TaskService taskService = new TaskService(repository);
        TaskController controller = new TaskController(taskService);

        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }

        String command = args[0];

        switch (command) {

            case "add":

                if (args.length < 2) {
                    System.out.println("Usage: add <description>");
                    break;
                }

                Task newTask = controller.addTask(args[1]);

                System.out.println("Task added successfully. ID: " + newTask.getTaskId());

                break;

            case "update":

                if (args.length < 3) {
                    System.out.println("Usage: update <id> <description>");
                    break;
                }

                Long updateId = Long.parseLong(args[1]);

                Task updatedTask =
                        controller.updateTask(updateId, args[2]);

                System.out.println("Task updated successfully. ID: "
                        + updatedTask.getTaskId());

                break;

            case "delete":

                if (args.length < 2) {
                    System.out.println("Usage: delete <id>");
                    break;
                }

                Long deleteId = Long.parseLong(args[1]);

                controller.deleteTask(deleteId);

                System.out.println("Task deleted successfully.");

                break;

            case "list":

                List<Task> tasks;

                if (args.length == 1) {

                    tasks = controller.findAllTasks();

                } else {

                    StatusType status;

                    switch (args[1].toLowerCase()) {

                        case "todo":
                            status = StatusType.TODO;
                            break;

                        case "done":
                            status = StatusType.DONE;
                            break;

                        case "in-progress":
                            status = StatusType.IN_PROGRESS;
                            break;

                        default:
                            System.out.println("Unknown status.");
                            return;
                    }

                    tasks = controller.findTasksByStatus(status);
                }

                if (tasks.isEmpty()) {
                    System.out.println("No tasks found.");
                } else {

                    for (Task task : tasks) {

                        System.out.println(
                                task.getTaskId()
                                        + " | "
                                        + task.getDescription()
                                        + " | "
                                        + task.getStatus());
                    }
                }

                break;

            case "mark-in-progress":

                if (args.length < 2) {
                    System.out.println("Usage: mark-in-progress <id>");
                    break;
                }

                Long progressId = Long.parseLong(args[1]);

                Task inProgressTask =
                        controller.markTaskInProgress(progressId);

                System.out.println("Task "
                        + inProgressTask.getTaskId()
                        + " is now IN_PROGRESS.");

                break;

            case "mark-done":

                if (args.length < 2) {
                    System.out.println("Usage: mark-done <id>");
                    break;
                }

                Long doneId = Long.parseLong(args[1]);

                Task doneTask =
                        controller.markTaskDone(doneId);

                System.out.println("Task "
                        + doneTask.getTaskId()
                        + " is now DONE.");

                break;

            default:
                System.out.println("Unknown command.");
        }
    }
}