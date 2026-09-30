package dawn.command;

import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskDateTime;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Command to display tasks occurring on a specific calendar date. */
public class ViewCommand extends Command {
    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

    private final TaskDateTime targetDateTime;

    public ViewCommand(TaskDateTime targetDateTime) {
        this.targetDateTime = targetDateTime;
    }

    public LocalDate getTargetDate() {
        return targetDateTime.toLocalDate();
    }

    public TaskDateTime getTargetDateTime() {
        return targetDateTime;
    }

    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) {
        LocalDate targetDate = targetDateTime.toLocalDate();
        String formattedDate = targetDate.format(DISPLAY_FORMATTER);

        StringBuilder sb = new StringBuilder();
        if (targetDateTime.hasTime()) {
            sb.append("Note: 'view' queries tasks for the entire day (")
              .append(formattedDate)
              .append(").\n\n");
        }

        StringBuilder taskListSb = new StringBuilder();
        taskListSb.append("Here are the tasks occurring on ")
                  .append(formattedDate)
                  .append(":\n");

        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            if (task.isOnDate(targetDate)) {
                count++;
                taskListSb.append(String.format("%d.%s\n", count, task));
            }
        }

        if (count == 0) {
            sb.append("No tasks occurring on ").append(formattedDate).append(".\n\n");
        } else {
            sb.append(taskListSb);
        }

        ui.showMessage(sb.toString());
    }
}
