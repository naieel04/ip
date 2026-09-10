import java.util.ArrayList;

/** Encapsulates the operations and state of the task collection. */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public int size() {
        return tasks.size();
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    public void addTask(Task task) throws DawnException {
        if (tasks.size() >= MAX_TASKS) {
            throw new DawnException("Dawn can store at most " + MAX_TASKS + " tasks.");
        }
        tasks.add(task);
    }
}
