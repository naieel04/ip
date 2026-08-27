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

    public static void printList(List<String> list,List<Boolean> markList){
        System.out.println("Here are the tasks in your list:");
        String marked="";
        for (int i = 0; i < list.size(); i++) {
            if(markList.get(i)){
                marked="X";
            }else{
                marked=" ";
            }
            System.out.printf("%d.[%s] %s\n",i+1,marked,list.get(i));
        }
    }
    public static void addTasks(){
        Scanner in = new Scanner(System.in);
        String input,output="";
        List<String> taskList = new ArrayList<>();
        List<Boolean> doneList = new ArrayList<>();

        input = in.nextLine();
        while (!input.equals("bye")) {
            output = "____________________________________________________________\n";
            System.out.println(output);
            if(input.equals("list")){
                printList(taskList,doneList);
            } else if (input.contains("unmark")) {
                int idx = Integer.parseInt(input.replaceAll("\\D",""))-1;
                if(idx>taskList.size()-1 || idx<0){
                    output = "Task not found\n";
                }else {
                    doneList.set(idx, false);
                    output = "OK, I've marked this task as not done yet:\n" +
                            "\t[ ] " + taskList.get(idx) + "\n";
                }
                System.out.println(output);

            } else if (input.contains("mark")) {
                int idx = Integer.parseInt(input.replaceAll("\\D",""))-1;
                if(idx>taskList.size()-1 || idx<0){
                    output = "Task not found\n";
                }else {
                    doneList.set(idx,true);
                    output = "Nice! I've marked this task as done:\n" +
                            "\t[X] " + taskList.get(idx) + "\n";
                }
                System.out.println(output);
            }else{
                if (taskList.size() < 100) {
                    taskList.add(input);
                    doneList.add(false);
                    output = "added: " + input + "\n";
                } else {
                    output = "Error, 100 tasks present";
                }
                System.out.println(output);
            }
            output="____________________________________________________________\n";
            System.out.println(output);
            input=in.nextLine();
        }
        output="____________________________________________________________\n";
        System.out.println(output);
    }



    public static void main(String[] args) {
        String banner = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";

        System.out.println(getIntroMessage());
        addTasks();
        System.out.println(getByeMessage());
    }
}
