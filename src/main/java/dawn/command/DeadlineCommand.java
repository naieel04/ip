package dawn.command;

import dawn.exception.DawnException;
import dawn.storage.Storage;
import dawn.task.Deadline;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/** Represents a command to add a deadline task. */
public class DeadlineCommand extends Command {
    private final String description;
    private final String by;

    public DeadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        Task task = new Deadline(description, by);
        tasks.addTask(task);
        storage.save(tasks);
        ui.showMessage("added: " + task.getDescription() + "\n\n");
    }
}
