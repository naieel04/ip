package dawn;

import dawn.command.CommandHandler;
import dawn.exception.DawnException;
import dawn.storage.Storage;
import dawn.ui.DawnUi;

/** Starts Dawn and coordinates the console application's lifecycle. */
public class Dawn {
    private final DawnUi ui;
    private final CommandHandler commandHandler;
    private final Storage storage;

    public Dawn() {
        this.storage = new Storage("data/dawn.txt");
        
        // Attempt to load existing tasks, falling back to a clean list on catastrophic I/O failure
        CommandHandler initHandler;
        try {
            initHandler = new CommandHandler(this.storage, this.storage.load());
        } catch (DawnException e) {
            System.out.println("Warning: Could not start with stored tasks. Initializing fresh list. (" + e.getMessage() + ")");
            initHandler = new CommandHandler(this.storage);
        }
        
        this.commandHandler = initHandler;
        this.ui = new DawnUi(this.commandHandler);
    }

    public void run() {
        ui.showIntro();
        boolean isExit = false;
        while (!isExit) {
            try {
                isExit = ui.processNextCommand();
            } catch (DawnException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("An unexpected error occurred: " + e.getMessage());
            }
        }
        ui.showBye();
    }

    public static void main(String[] args) {
        new Dawn().run();
    }
}
