package dawn.command;

import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Command to display tasks occurring on a specific calendar date. */
public class ScheduleCommand extends Command {
    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final LocalDate targetDate;

    public ScheduleCommand(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) {
        StringBuilder sb = new StringBuilder(
                "Here are the tasks occurring on " + targetDate.format(DISPLAY_FORMATTER) + ":\n");
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            if (task.isOnDate(targetDate)) {
                count++;
                sb.append(String.format("%d.%s\n", count, task));
            }
        }
        if (count == 0) {
            ui.showMessage("No tasks occurring on " + targetDate.format(DISPLAY_FORMATTER) + ".\n\n");
        } else {
            ui.showMessage(sb.toString());
        }
    }
}
