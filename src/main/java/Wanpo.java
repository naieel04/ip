import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Wanpo {
    public static String getIntroMessage(){
        String nameBanner =  "██╗    ██╗ █████╗ ███╗   ██╗██████╗  ██████╗ \n" +
                "██║    ██║██╔══██╗████╗  ██║██╔══██╗██╔═══██╗\n" +
                "██║ █╗ ██║███████║██╔██╗ ██║██████╔╝██║   ██║\n" +
                "██║███╗██║██╔══██║██║╚██╗██║██╔═══╝ ██║   ██║\n" +
                "╚███╔███╔╝██║  ██║██║ ╚████║██║     ╚██████╔╝\n" +
                " ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝      ╚═════╝ \n";
        String greeting = "____________________________________________________________\n" +
                nameBanner+
                "Hello! I'm Wanpo.\n" +
                "What can I do for you?\n" +
                "____________________________________________________________\n" ;

        String introMessage = nameBanner+greeting;
        return introMessage;
    }
    public static String getByeMessage(){
        String byeMessage = "Bye. Hope to see you again soon!\n" +
                "____________________________________________________________";
        return byeMessage;
    }
    public static void echo(){
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        String output;
        while (!input.equals("bye")) {
            output = "____________________________________________________________\n" +
                    input + "\n____________________________________________________________";
            System.out.println(output);
            input = in.nextLine();
        }
        output = "____________________________________________________________\n";
        System.out.println(output);
    }


    public static void main(String[] args) {
        String banner = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";

        System.out.println(getIntroMessage());
        echo();
        System.out.println(getByeMessage());
    }
}
