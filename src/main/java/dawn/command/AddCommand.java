package dawn.command;

import dawn.exception.DawnException;
import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/** Represents a command to add a task to the task list. */
public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        tasks.addTask(task);
        storage.save(tasks);
        ui.showMessage("added: " + task.getDescription() + "\n\n");
    }
}
