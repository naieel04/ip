package dawn.storage;

import dawn.exception.DawnException;
import dawn.task.Deadline;
import dawn.task.Event;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.task.ToDo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    /**
     * Loads tasks from the persistent text file.
     * 
     * @return a populated TaskList based on the saved data.
     * @throws DawnException if a read failure occurs.
     */
    public TaskList load() throws DawnException {
        TaskList taskList = new TaskList();
        
        if (Files.notExists(filePath)) {
            return taskList; // Return empty list if no file exists yet
        }

        try {
            List<String> lines = Files.readAllLines(filePath, java.nio.charset.StandardCharsets.UTF_8);
            for (String line : lines) {
                // Strip UTF-8 BOM if present at the start of the file
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }

                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    Task task = parseLineToTask(line);
                    taskList.addTask(task); // Suppresses MAX_TASKS exception safely if reading old valid limit
                } catch (Exception e) {
                    System.out.println("Warning: Corrupted task line skipped: [" + line + "] - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new DawnException("Failed to load tasks from file: " + e.getMessage());
        }

        return taskList;
    }

    private Task parseLineToTask(String line) throws Exception {
        // Example format: T | 1 | read book
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            throw new Exception("Missing essential task components.");
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        switch (type) {
        case "T":
            return new ToDo(description, isDone);
        case "D":
            if (parts.length < 4) {
                throw new Exception("Deadline is missing the due date.");
            }
            return new Deadline(description, parts[3].trim(), isDone);
        case "E":
            if (parts.length < 5) {
                throw new Exception("Event is missing start or end dates.");
            }
            return new Event(description, parts[3].trim(), parts[4].trim(), isDone);
        default:
            throw new Exception("Unknown task type identifier: " + type);
        }
    }
}
