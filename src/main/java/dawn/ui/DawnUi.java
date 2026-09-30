package dawn.ui;

import java.util.Scanner;

/** Handles console input and displays Dawn's messages and command feedback. */
public class DawnUi {
    public static final String TXT_NAME_BANNER = """
            ██████╗  █████╗ ██╗    ██╗███╗   ██╗
            ██╔══██╗██╔══██╗██║    ██║████╗  ██║
            ██║  ██║███████║██║ █╗ ██║██╔██╗ ██║
            ██║  ██║██╔══██║██║███╗██║██║╚██╗██║
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
    public static final String INTRO_MESSAGE = MAX_LINE + IMG_NAME_BANNER
            + "Hello! I'm Dawn.\nWhat can I do for you?\n" + MAX_LINE;
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

    /** Displays an error message wrapped in divider lines. */
    public void showError(String error) {
        System.out.println(error + "\n\n" + MAX_LINE);
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
