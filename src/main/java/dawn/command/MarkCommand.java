package dawn.command;

import dawn.exception.DawnException;
import dawn.parser.Parser;
import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/** Represents a command to mark a task as completed. */
public class MarkCommand extends Command {
    private final int taskIndex;

    public MarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new DawnException("Task number not found. Use: " + Parser.MARK_USAGE);
        }
        Task task = tasks.getTask(taskIndex);
        task.setDone(true);
        storage.save(tasks);
        ui.showMessage("Nice! I've marked this task as done:\n\t" + task + "\n\n");
    }
}
