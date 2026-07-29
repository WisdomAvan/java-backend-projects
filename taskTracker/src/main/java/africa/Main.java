package africa;

import africa.wisdom.taskTracker.controllers.TaskController;
import africa.wisdom.taskTracker.data.models.Task;
import africa.wisdom.taskTracker.persistence.implementation.JsonTaskRepository;
import africa.wisdom.taskTracker.persistence.repositories.TaskRepository;
import africa.wisdom.taskTracker.services.TaskService;

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

        if (command.equals("add")) {

            String description = args[1];

            Task task = controller.addTask(description);

            System.out.println("Task added successfully. ID: " + task.getTaskId());
        }

        if (command.equals("update")) {

            Long id = Long.parseLong(args[1]);

            String description = args[2];

            Task updatedTask = controller.updateTask(id, description);

            System.out.println("Task updated successfully. ID: " + updatedTask.getTaskId());
        }
    }
}