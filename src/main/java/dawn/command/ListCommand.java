package dawn.command;

import dawn.storage.Storage;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/** Represents a command to display all tasks in the task list. */
public class ListCommand extends Command {
    /**
     * Executes the list command, displaying all currently stored tasks in sequence.
     *
     * @param tasks the current list of tasks
     * @param ui the user interface component
     * @param storage the file storage system
     */
    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) {
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d.%s\n", i + 1, tasks.getTask(i)));
        }
        ui.showMessage(sb.toString());
    }
}
