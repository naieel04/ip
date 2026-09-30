package dawn.task;

/** Subclass Deadline: A task with a due date and description. */
public class Deadline extends Task {
    private TaskDateTime dueDate;

    public Deadline(String description, TaskDateTime dueDate) {
        super(description);
        this.dueDate = dueDate;
    }

    public Deadline(String description, TaskDateTime dueDate, boolean isDone) {
        super(description, isDone);
        this.dueDate = dueDate;
    }

    public TaskDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(TaskDateTime dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toFileString() {
        return "D | " + super.toFileString() + " | " + dueDate.toStorageString();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + dueDate.toDisplayString() + ")";
    }
}
