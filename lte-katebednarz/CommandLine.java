import java.util.*;

public class CommandLine {
    // data members
    public static String line;
    public static String command;
    public static ArrayList<String> arguments;

    // member functions

    // reads and parses the command entered
    public static String getCommand(){
        arguments = new ArrayList<String>();
        Scanner input = new Scanner(System.in);
        if(input.hasNext()){
            line = input.nextLine();
        }
        if(line == ""){
            return "null";
        } else {
            StringTokenizer tok = new StringTokenizer(line, " ");
            command = tok.nextToken();
            while(tok.hasMoreTokens()){
                arguments.add(tok.nextToken());
            }
            return command;
        }
    }
}