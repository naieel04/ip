package dawn.command;

import dawn.exception.DawnException;
import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.task.ToDo;
import dawn.ui.DawnUi;

/** Represents a command to add a todo task. */
public class TodoCommand extends Command {
    private final String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        Task task = new ToDo(description);
        tasks.addTask(task);
        storage.save(tasks);
        ui.showMessage("added: " + task.getDescription() + "\n\n");
    }
}
