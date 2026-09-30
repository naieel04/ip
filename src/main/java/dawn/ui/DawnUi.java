package dawn.ui;

import java.util.Scanner;

/** Handles console input and displays Dawn's messages and command feedback. */
public class DawnUi {
    public static final String TXT_NAME_BANNER = """
            ██████╗  █████╗ ██╗    ██╗████╗   ██╗
            ██╔══██╗██╔══██╗██║    ██║██████╗  ██║
            ██║  ██║███████║██║ █╗ ██║██╔████╗ ██║
            ██║  ██║██╔══██║██║███╗██║██║╚████╗██║
            ██████╔╝██║  ██║╚███╔███╔╝██║ ╚████║
            ╚═════╝ ╚═╝  ╚═╝ ╚══╝╚══╝ ╚═╝  ╚═══╝
            \n""";
    public static final String IMG_NAME_BANNER = """
            
                                         ..#+++.-+++##+.                    \s
                                     .#...##..............+-                \s
                                  .+.....##+....................            \s
                                ..................................          \s
                              -............................   ..            \s
                            ..     ...................... +##########       \s
                           .########. .......  ........ -#############+     \s
                          ###########+  ... .##. ... . -################    \s
                         ###### #######.#. ###### .##-.#######. #########   \s
                        ######  . #####.+#####+###### #####. ..   #######.  \s
                        #####  +###+####.#- +###+..#++##### ####   #######  \s
                       -####.  +###+####  #########  #####- ####   -######  \s
                       +####.   ##+ #### ########### #####.   .    .######. \s
                       +####.       ####.##########..#####.        +######. \s
               ##      .####.      .####.-#######-.#.######        #######. \s
              ####      #####      #####+-#+---+###.########      ########  \s
            ##+#+###    ######+   ######## +###### ######################.  \s
              ##+#       ####################...#########################   \s
                #         ##############################################    \s
                          .###########################################+     \s
                            #########################################       \s
                             .######################-       .######         \s
                               .####-  .....####-.+++-......-.. .           \s
                                  .#+........+.+#.................###.      \s
                            ###.+#................................######.   \s
                        +#####.++.............  ............... .##..#####. \s
                      .####### #............. #- ............ .##..+####### \s
                      ########+.#.......... .####+   ....   +##+ +#########.\s
                     .########+   ......  .########+.#####.###+ ########### \s
                      #########-.## ..-##+.#######.+######++##..##########  \s
                      .######### #+-###### ####### ########.##..########.   \s
                        .######+ ##.###### ######## ######.#### ######.     \s
                          .####.-##+.#### ##########- .- .###### .###.      \s
                          .++. #######++##########################.         \s
                               ###################################...       \s
                               -################################## .        \s
                                #################################.          \s
                                 ###############################.           \s
                                  #############################             \s
                                  . ########################+..+.           \s
                               -###-.##.                 .+##.-###.         \s
                             .########+++                -+++#######        \s
                           .-.+#######+.                  .########+#.      \s
                           -#+.+###+.                       .+####+#+..     \s
                             .-                                 +##..#+     \s
            \n""";

    public static final String MAX_LINE = "____________________________________________________________\n";
    public static final String COMMAND_GUIDE = """
            Here are the commands you can use:
              todo [description]                     - Add a todo task
              deadline [description] /by [due date]  - Add a deadline task
              event [desc] /from [start] /to [end]   - Add an event task
              list                                   - List all tasks
              mark [task number]                     - Mark a task as done
              unmark [task number]                   - Mark a task as not done
              delete [task number]                   - Delete a task
              view [date]                            - View tasks on a specific date
              bye                                    - Exit the application""";

    public static final String INTRO_MESSAGE = MAX_LINE + IMG_NAME_BANNER
            + "Hello! I'm Dawn.\nWhat can I do for you?\n\n"
            + COMMAND_GUIDE + "\n"
            + MAX_LINE;
    public static final String BYE_MESSAGE = "Bye. Hope to see you again soon!\n" + MAX_LINE;

    private final Scanner scanner;

    public DawnUi() {
        this(new Scanner(System.in));
    }

    public DawnUi(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Displays the application welcome message and banner. */
    public void showIntro() {
        System.out.println(INTRO_MESSAGE);
    }

    /** Displays the goodbye message upon exiting. */
    public void showBye() {
        System.out.println(BYE_MESSAGE);
    }

    /** Displays a divider line between commands. */
    public void showLine() {
        System.out.println(MAX_LINE);
    }

    /** Displays a response message. */
    public void showMessage(String message) {
        System.out.print(message);
    }

    /** Displays an error message. */
    public void showError(String error) {
        System.out.print(error + "\n\n");
    }

    /** Displays a warning when the task storage file cannot be loaded. */
    public void showLoadingError() {
        System.out.println("Warning: Could not start with stored tasks. Initializing fresh list.");
    }

    /**
     * Reads the next command line from input.
     *
     * @return the raw input line, or {@code null} if input stream is exhausted
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine();
    }
}
