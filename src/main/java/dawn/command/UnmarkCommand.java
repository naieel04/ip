package dawn.command;

import dawn.exception.DawnException;
import dawn.parser.Parser;
import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/** Represents a command to mark a task as not completed yet. */
public class UnmarkCommand extends Command {
    private final int taskIndex;

    public UnmarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new DawnException("Task number not found. Use: " + Parser.UNMARK_USAGE);
        }
        Task task = tasks.getTask(taskIndex);
        task.setDone(false);
        storage.save(tasks);
        ui.showMessage("OK, I've marked this task as not done yet:\n\t" + task + "\n\n");
    }
}
