package dawn.storage;

import dawn.exception.DawnException;
import dawn.task.TaskList;
import dawn.task.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Handles saving and loading tasks to and from a persistent text file. */
public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /** 
     * Saves the current task list to the file, creating the file and parent directories if they don't exist. 
     */
    public void save(TaskList taskList) throws DawnException {
        try {
            if (Files.notExists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.getTask(i);
                sb.append(task.toFileString()).append(System.lineSeparator());
            }
            
            Files.writeString(filePath, sb.toString(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DawnException("Failed to save tasks to file: " + e.getMessage());
        }
    }
}
