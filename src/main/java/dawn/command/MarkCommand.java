package dawn.command;

import dawn.exception.DawnException;
import dawn.parser.CommandWord;
import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/** Represents a command to mark a task as completed. */
public class MarkCommand extends Command {
    private final int taskIndex;

    /**
     * Constructs a command to mark the task at the specified index as completed.
     *
     * @param taskIndex the zero-based index of the task to mark
     */
    public MarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Executes the mark command, updates the task's status, and saves to storage.
     *
     * @param tasks the current list of tasks
     * @param ui the user interface component
     * @param storage the file storage system
     * @throws DawnException if the index is out of bounds
     */
    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new DawnException("I couldn't find that task number. Use: " + CommandWord.MARK.usage());
        }
        Task task = tasks.getTask(taskIndex);
        boolean wasDone = task.isDone();
        task.setDone(true);
        try {
            storage.save(tasks);
        } catch (DawnException e) {
            task.setDone(wasDone);
            throw e;
        }
        ui.showMessage("Nice! Marked this task as done:\n\t" + task + "\n\n");
    }
}
