package dawn.command;

import dawn.storage.Storage;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/** Represents a command to exit the application. */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) {
        // No execution action needed for exit; Dawn handles bye presentation
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
