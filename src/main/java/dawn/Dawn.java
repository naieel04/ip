package dawn;

import dawn.command.CommandHandler;
import dawn.exception.DawnException;
import dawn.storage.Storage;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/** Starts Dawn and coordinates the console application's lifecycle. */
public class Dawn {
    private final DawnUi ui;
    private final Storage storage;
    private TaskList tasks;
    private final CommandHandler commandHandler;

    /**
     * Initializes Dawn with persistent storage at the given file path.
     *
     * @param filePath the path to the tasks file
     */
    public Dawn(String filePath) {
        this.ui = new DawnUi();
        this.storage = new Storage(filePath);
        try {
            this.tasks = this.storage.load();
        } catch (DawnException e) {
            ui.showLoadingError();
            this.tasks = new TaskList();
        }
        this.commandHandler = new CommandHandler(this.storage, this.tasks);
    }

    /** Initializes Dawn with the default storage file path. */
    public Dawn() {
        this("data/dawn.txt");
    }

    /** Runs the main application event loop until the exit command is received. */
    public void run() {
        ui.showIntro();
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            if (fullCommand == null) {
                break;
            }
            ui.showLine();
            try {
                String feedback = commandHandler.handleCommand(fullCommand);
                if (feedback == null) {
                    isExit = true;
                } else {
                    ui.showMessage(feedback);
                    ui.showLine();
                }
            } catch (DawnException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("An unexpected error occurred: " + e.getMessage());
            }
        }
        ui.showBye();
    }

    public static void main(String[] args) {
        new Dawn("data/dawn.txt").run();
    }
}
