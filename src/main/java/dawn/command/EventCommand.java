package dawn.command;

import dawn.exception.DawnException;
import dawn.storage.Storage;
import dawn.task.Event;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/** Represents a command to add an event task. */
public class EventCommand extends Command {
    private final String description;
    private final String from;
    private final String to;

    public EventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        Task task = new Event(description, from, to);
        tasks.addTask(task);
        storage.save(tasks);
        ui.showMessage("added: " + task.getDescription() + "\n\n");
    }
}
