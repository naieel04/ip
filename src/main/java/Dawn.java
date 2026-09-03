public class Dawn {
    private final DawnUi ui;
    private final CommandHandler commandHandler;

    public Dawn() {
        this.commandHandler = new CommandHandler();
        this.ui = new DawnUi(commandHandler);
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
