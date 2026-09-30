package dawn.command;

import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

import java.util.ArrayList;

/** Command to find tasks containing a specific search keyword in their description. */
public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) {
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        if (matchingTasks.isEmpty()) {
            ui.showMessage(String.format("No tasks found matching '%s'.\n\n", keyword));
            return;
        }

        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            sb.append(String.format("%d.%s\n", i + 1, matchingTasks.get(i)));
        }
        ui.showMessage(sb.toString());
    }
}
