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

    /**
     * Constructs a command to delete a task at the specified index.
     *
     * @param taskIndex the zero-based index of the task to drop
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Removes the task from the collection, saves the updated list, and returns a success message.
     *
     * @param tasks the current list of tasks
     * @param ui the user interface component
     * @param storage the file storage system
     * @throws DawnException if the index is out of bounds
     */
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
