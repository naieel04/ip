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
        this.commandHandler = new CommandHandler(this.storage);
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
