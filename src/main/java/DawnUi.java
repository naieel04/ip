import java.util.Scanner;

public class DawnUi {
    public static final String NAME_BANNER = """
                                     :::xxxxxxxxxxx;:::.              \s
                               .xxxx+;:..............:;xx;            \s
                            ;xx+;::::.....................;x+         \s
                         .xx+;:;++++;:.................;++++xxx:      \s
                        .x+:;+;.     .;;.............::        .xx    \s
                       +x;:+:          .;..........:;.           .x+  \s
                      x+::x..+.         ;:.........;:        .xX:  xx \s
                     x+;:+..$$X         ;;.........;.        .X$;  .x;\s
                    ;x;:;+  .:          ;;.........+.               +;\s
                   .x;::;;              +:.........;;               ;;\s
                   x+:;:;x             +;...........;.              +;\s
                  .x;:;::;X.         ;+..............;;            ;x;\s
                  ++;:::;::;x+;:;;xx;..................+;.        xxx \s
                  ++:::::;::::...........................:+xx+++++::x;\s
                  ++::;::;:;::......................................x;\s
                  +x;::;::::::.......;xxx+;........;+XXXXx;........:x;\s
                   x;:::;:;:::......+XxxxxxxXXXXXXxxxxxxxxX:.......;x.\s
                   +x;:::::;:;:.....+xxxxxxxxxxxxxxxxxxxxxX:.......+x \s
                   .xx;:;::::::.....;Xxxxxxxxxxxxxxxxxxxxxx:......;x. \s
                     +x;:;;:;:;:.....:x+x+x+xx+++xxxxxx+X+.......:x;  \s
                      xx;::::;:::......:;x++++++++;++x+;........;x.   \s
                  :+:x;xxx;::::;::............................:xx     \s
              .xx+;;:;;;+xxx;;:::;::........................;xx.      \s
             ;x;:::;::::::;xxxxx;;;:::..................:;xxx.        \s
            .x;::::::;::;:::+xx;;;+XXxx++;;;;;;;;;;;xx+;;;.;x:        \s
            x;:;:;:::::::;:;;x;...;+x  ;; ...  ..:.    . ;:.;x;       \s
            x;:::::++xx;::::;+....;X. :;.       .:.     ..;..;X.      \s
            x;:;:::::::;x:::++...:x+  +. .  . . .:.. .   .x..:x.      \s
            :x;::;::::::x;::;+..:xx; .+........:::::..:::.+:.:x.      \s
             ;x;::;::;:;x:;::;xxX+x; ;; .........::... . .+;+x.       \s
              ;xx;;;;+xx;;;:;;;+x+x; .+.       ..:.    . ;+.          \s\n""";
    public static final String MAX_LINE = "____________________________________________________________\n";
    public static final String INTRO_MESSAGE = MAX_LINE + NAME_BANNER + "Hello! I'm Dawn.\nWhat can I do for you?\n" + MAX_LINE;
    public static final String BYE_MESSAGE = "Bye. Hope to see you again soon!\n" + MAX_LINE;

    private final Scanner scanner;
    private final CommandHandler commandHandler;

    public DawnUi(CommandHandler commandHandler) {
        this(commandHandler, new Scanner(System.in));
    }

    public DawnUi(CommandHandler commandHandler, Scanner scanner) {
        this.commandHandler = commandHandler;
        this.scanner = scanner;
    }

    public void showIntro() {
        System.out.println(INTRO_MESSAGE);
    }

    public void showBye() {
        System.out.println(BYE_MESSAGE);
    }

    public void showError(String error) {
        System.out.println(error + "\n\n" + MAX_LINE);
    }

    public void printDivider() {
        System.out.println(MAX_LINE);
    }

    /**
     * Reads the next command line from input and passes it to CommandHandler.
     *
     * @return true if exit command was issued; false otherwise
     */
    public boolean processNextCommand() throws DawnException {
        if (!scanner.hasNextLine()) {
            return true;
        }
        String input = scanner.nextLine();
        System.out.println(MAX_LINE);
        
        String feedback = commandHandler.handleCommand(input);
        if (feedback == null) {
            return true;
        }
        
        System.out.print(feedback);
        System.out.println(MAX_LINE);
        return false;
    }

    // Backwards-compatible methods if needed
    public String getIntroMessage() {
        return INTRO_MESSAGE;
    }

    public String getByeMessage() {
        return BYE_MESSAGE;
    }
}