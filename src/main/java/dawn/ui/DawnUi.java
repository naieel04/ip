package dawn.ui;

import dawn.parser.CommandWord;

import java.util.Scanner;

/** Handles user interactions, message rendering, and error formatting. */
public class DawnUi {
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
    public static final String COMMAND_GUIDE = CommandWord.commandGuide();

    public static final String INTRO_MESSAGE = MAX_LINE + "\n" + IMG_NAME_BANNER
            + "Hello! I'm Dawn.\nWhat can I do for you?\n\n"
            + COMMAND_GUIDE + "\n"
            + MAX_LINE;
    public static final String BYE_MESSAGE = "Bye. Hope to see you again soon!\n" + MAX_LINE;

    private final Scanner scanner;

    /** Constructs a user interface bound to the standard system input stream. */
    public DawnUi() {
        this(new Scanner(System.in));
    }

    /**
     * Constructs a user interface capturing input from a custom scanner.
     *
     * @param scanner the scanner bound to this interface
     */
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

    /**
     * Displays a response message.
     *
     * @param message the content string to echo back
     */
    public void showMessage(String message) {
        System.out.print(message);
    }

    /**
     * Displays an error message formatting appropriately.
     *
     * @param error the specific error text
     */
    public void showError(String error) {
        System.out.println(error + "\n");
    }

    /**
     * Displays a terminal failure message when file persistence is irrecoverable.
     */
    public void showLoadingError() {
        System.out.println("No saved tasks found or file is corrupted. Starting fresh!");
    }

    /**
     * Reads the next non-empty command line from the console.
     *
     * @return the raw user command line
     */
    public String readCommand() {
        return scanner.nextLine();
    }
}
