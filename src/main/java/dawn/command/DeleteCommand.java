package dawn.command;

import dawn.exception.DawnException;
import dawn.parser.Parser;
import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/** Represents a command to delete a task from the task list. */
public class DeleteCommand extends Command {
    private final int taskIndex;

    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new DawnException("Task number not found. Use: " + Parser.DELETE_USAGE);
        }
        Task removedTask = tasks.removeTask(taskIndex);
        storage.save(tasks);
        ui.showMessage("Noted. I've removed this task:\n\t" + removedTask
                + "\nNow you have " + tasks.size() + " tasks in the list\n\n");
    }
}
