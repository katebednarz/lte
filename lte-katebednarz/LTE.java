import java.util.*;

class LTE {
    public static Buffer main;
    public static Buffer clipboard = new Buffer();
    public static boolean exiting = false;
    public static boolean lineNumbersOn = true;
    public static int numberOfCommandsExecuted = 0;
    public static int count = 0;

    public static void main(String[] args) {
        if(args.length > 1){
            System.out.println("There are too many arguments");
        } else if(args.length == 1){
            main = new Buffer(args[0]);
        } else {
            main = new Buffer();
            main.filename = "new file";
        }

        while(!exiting){
            processCommand();
        }
    }

    // finds which command was entered
    public static void processCommand() {
        // prints a prompt
        if(lineNumbersOn){
            System.out.print("LTE:" + main.filename + ":" + numberOfCommandsExecuted + ":" + (main.lines.getIndex() + 1) + "> ");
        } else {
            System.out.print("LTE:" + main.filename + ":" + numberOfCommandsExecuted + "> ");
        }

        // handles command
        numberOfCommandsExecuted++;
        switch (CommandLine.getCommand()){
            case "h":
                helpDriver();
                break;
            case "r":
                readFileDriver(CommandLine.arguments.get(0));
                break;
            case "w":
                writeDriver();
                break;
            case "f":
                changeNameDriver(CommandLine.arguments.get(0));
                break;
            case "q":
                quitDriver();
                break;
            case "q!":
                quitWithoutSavingDriver();
                break;
            case "t":
                goToFirstLineDriver();
                break;
            case "b":
                goToLastLineDriver();
                break;
            case "g":
                try {
                    goToLineDriver(Integer.parseInt(CommandLine.arguments.get(0)));
                    break;
                } catch (Exception e) {
                    System.out.println(CommandLine.arguments.get(0));
                    System.out.println("Error: Invalid Input");
                    break;
                }
            case "-":
                goToPreviousLineDriver();
                break;
            case "+":
                goToNextLineDriver();
                break;
            case "=":
                printCurrentLineNumberDriver();
                break;
            case "n":
                toggleLineNumbersDriver();
                break;
            case "#":
                printNumberOfLinesAndCharactersDriver();
                break;
            case "p":
                printCurrentLineDriver();
                break;
            case "pr":
                try {
                    printRangeDriver(Integer.parseInt(CommandLine.arguments.get(0)), 
                        Integer.parseInt(CommandLine.arguments.get(1)));
                    break;
                } catch (Exception e) {
                    System.out.println("Error: Invalid Input");
                    break;
                }
            case "?":
                backwardsSearchDriver(CommandLine.arguments.get(0));
                break;
            case "/":
                forwardSearchDriver(CommandLine.arguments.get(0));
                break;
            case "s":
                substituteTextDriver(CommandLine.arguments.get(0), CommandLine.arguments.get(1));
                break;
            case "sr":
                try {
                    substituteTextInRangeDriver(CommandLine.arguments.get(0),
                        CommandLine.arguments.get(1),
                        Integer.parseInt(CommandLine.arguments.get(2)),
                        Integer.parseInt(CommandLine.arguments.get(3)));
                    break;
                } catch (Exception e) {
                    System.out.println("Error: Invalid Input");
                    break;
                }
            case "d":
                cutLineDriver();
                break;
            case "dr":
                try {
                    cutLinesInRangeDriver(Integer.parseInt(CommandLine.arguments.get(0)),
                        Integer.parseInt(CommandLine.arguments.get(1)));
                    break;
                } catch (Exception e) {
                    System.out.println("Error: Invalid Input");
                    break;
                }
            case "c":
                copyDriver();
                break;
            case "cr":
                try {
                    copyLinesInRangeDriver(Integer.parseInt(CommandLine.arguments.get(0)),
                        Integer.parseInt(CommandLine.arguments.get(1)));
                    break;
                } catch (Exception e) {
                    System.out.println("Error: Invalid Input");
                    break;
                }
            case "pa":
                pasteAboveDriver();
                break;
            case "pb":
                pasteBelowDriver();
                break;
            case "ia":
                insertAboveDriver();
                break;
            case "ic":
                insertCurrentDriver();
                break;
            case "ib":
                insertBelowDriver();
                break;
            default:
                System.out.println("Unkown Command");
        }
    }

    /////////////////////////////
    // METHODS TO HANDLE COMMANDS
    /////////////////////////////
    
    // h - display help
    public static void helpDriver(){
        System.out.println("h               display help        f   filespec    change name of buffer");
        System.out.println("r   filespec    read a file         w               write file");
        System.out.println("q               quit editor         q!              quit without saving");
        System.out.println("t               go to first line    b               go to last line");
        System.out.println("g   num         go to line num      =               print current line number");
        System.out.println("-               go to previous line +               go to next line");
        System.out.println("n                           toggle line numbers");
        System.out.println("#                           print number of lines and characters");
        System.out.println("p                           print current line");
        System.out.println("pr  start stop              print lines from start to stop");
        System.out.println("?   pattern                 search backwards for pattern");
        System.out.println("/   pattern                 search forwards for pattern");
        System.out.println("s   text1 text2             substitute all text1 with text2 on current line");
        System.out.println("sr  text1 text2 start stop  substitute all text1 with text2 from start to stop");
        System.out.println("d                           delete current line");
        System.out.println("dr  start stop              delete lines from start to stop");
        System.out.println("c                           copy current line");
        System.out.println("cr  start stop              copy lines between start and stop");
        System.out.println("pa                          paste above current line");
        System.out.println("pb                          paste below current line");
        System.out.println("ia                          insert above current line until . appears by itself");
        System.out.println("ic                          insert at current line until . appears by itself");
        System.out.println("ib                          insert below current line until . appears by itself");
    }

    // r - read a file into the current buffer
    public static void readFileDriver(String filespec){
        if(!(main.lines.isEmpty())){
            main.lines.clear();
        }
        Scanner scr = new Scanner(filespec);
        if(main.dirty){
            System.out.println("Do you want to save your changes before exiting? y/n");
        }
        if(scr.nextLine() == "y"){
            writeDriver();
        }
        main.filename = filespec;
        main.lines.clear();
        main.read();
        scr.close();
    }

    // w - write the current buffer to a file on disk
    public static void writeDriver(){
        int originalIndex = main.lines.getIndex();
        main.write();
        main.lines.seek(originalIndex);
    }

    // f - change the name of the current buffer
    public static void changeNameDriver(String filespec) {
        main.filename = filespec;
        main.dirty = true;
    }

    // q - quit the line editor
    public static void quitDriver(){
        Scanner input = new Scanner(System.in);
        if(main.dirty){
            System.out.println("Do you want to save your changes before exiting? y/n");
            String answer = input.nextLine();
            if(answer.equalsIgnoreCase("y")){
                main.write();
            }
        }
        input.close();
        quitWithoutSavingDriver();
    }

    // q! - quit the line editor without saving
    public static void quitWithoutSavingDriver(){
        System.exit(0);
    }

    // t - go to first line in the buffer
    public static void goToFirstLineDriver(){
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else {
            main.lines.first();
        }
    }

    // b - go to last line in the buffer
    public static void goToLastLineDriver(){
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else {
            main.lines.last();
        }
    }

    // g - go to line num in the buffer
    public static void goToLineDriver(int num){
        // changes the line number given to the index we are moving to
        num--;
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else if(num < 0 || num > main.lines.getSize()){
            System.out.println("==>> RANGE ERROR - num MUST BE [1.." + main.lines.getSize() + "] <<==");
        } else {
            main.lines.seek(num);
        }
    }

    // - - go to the previous line
    public static void goToPreviousLineDriver(){
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else if(main.lines.atFirst()){
            System.out.println("==>> ALREADY AT TOP OF BUFFER <<==");
        } else {
            main.lines.previous();
        }
    }

    // + - go to the next line
    public static void goToNextLineDriver(){
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else if(main.lines.atLast()){
            System.out.println("==>> ALREADY AT BOTTOM OF BUFFER <<==");
        } else {
            main.lines.next();
        }
    }

    // = - print the current line number
    public static void printCurrentLineNumberDriver(){
        System.out.println(main.lines.getIndex() + 1);
    }

    // n - toggle line number displayed
    public static void toggleLineNumbersDriver(){
        lineNumbersOn = !lineNumbersOn;
    }

    // # - print the number of lines and characters in the buffer
    public static void printNumberOfLinesAndCharactersDriver(){
        int numberOfCharacters = 0;
        int originalIndex = main.lines.getIndex();
        String currentLine;

        for(int i = 1; i <= main.lines.getSize(); i++){
            goToLineDriver(i);
            currentLine = main.lines.getData();
            numberOfCharacters = numberOfCharacters + currentLine.length();
        }
        System.out.println("There are " + main.lines.getSize() + " lines");
        System.out.println("There are " + numberOfCharacters + " characters");
        main.lines.seek(originalIndex);
    }

    // p - print the current line
    public static void printCurrentLineDriver(){
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else {
            System.out.println(main.lines.getData());
        }
    }

    // pr - print several lines
    public static void printRangeDriver(int start, int stop){
        int originalIndex = main.lines.getIndex();
        int size = main.lines.getSize();
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else if (start < 1|| start > size || stop < 1 || stop > size){
            System.out.println("==>> RANGE ERROR - start and size MUST BE [1.." + size + "] ==<<");
            return;
        } else {
            for(int i = start; i <= stop; i++){
                goToLineDriver(i);
                printCurrentLineDriver();
            }
        }
        main.lines.seek(originalIndex);
    }

    // ? - search backwards for a pattern
    public static void backwardsSearchDriver(String pattern){
        int originalIndex = main.lines.getIndex();
        Scanner input = new Scanner(System.in);
        boolean keepSearching = true;
        boolean found = false;
        String currentLine = "";
        String answer = "";

        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY ==<<");
        } else if(main.lines.atFirst()){
            System.out.println("==>> ALREADY AT TOP OF BUFFER <<==");
            return;
        } else {
            while(!main.lines.atFirst() && keepSearching){
                main.lines.previous();
                currentLine = main.lines.getData();
                StringTokenizer tok = new StringTokenizer(currentLine, " ");
                while(tok.hasMoreTokens()){
                    if(tok.nextToken().equals(pattern)){
                        System.out.println("Match found on line " + (main.lines.getIndex() + 1));
                        System.out.println("Do you want to keep searching? y/n");
                        answer = "";
                        while(!(answer.equalsIgnoreCase("y")) && !(answer.equalsIgnoreCase("n"))){
                            answer = input.nextLine();
                            if(!(answer.equalsIgnoreCase("y")) && !(answer.equalsIgnoreCase("n"))){
                                System.out.print("Error: Invalid Input: (y/n)>");
                            }
                        }
                        System.out.println();
                        if(answer.equalsIgnoreCase("y")){
                            continue;
                        } else if(answer.equalsIgnoreCase("n")){
                            keepSearching = false;
                            found = true;
                            break;
                        }
                    }
                }
            }
        }
        if(!found){
            System.out.println("==>> STRING " + pattern + " NOT FOUND <<==");
            main.lines.seek(originalIndex);
            return;
        }
    }

    // / - search forwards for a pattern
    public static void forwardSearchDriver(String pattern){
        int originalIndex = main.lines.getIndex();
        Scanner input = new Scanner(System.in);
        boolean keepSearching = true;
        boolean found = false;
        String currentLine = "";
        String answer = "";

        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else if(main.lines.atLast()){
            System.out.println("==>> ALREADY AT BOTTOM OF BUFFER <<==");
            return;
        } else {
            while(!main.lines.atLast() && keepSearching){
                main.lines.next();
                currentLine = main.lines.getData();
                StringTokenizer tok = new StringTokenizer(currentLine, " ");
                while(tok.hasMoreTokens()){
                    if(tok.nextToken().equals(pattern)){
                        System.out.println("Match found on line " + (main.lines.getIndex() + 1));
                        System.out.println("Do you want to keep searching? y/n");
                        answer = "";
                        while(!(answer.equalsIgnoreCase("y")) && !(answer.equalsIgnoreCase("n"))){
                            answer = input.nextLine();
                            if(!(answer.equalsIgnoreCase("y")) && !(answer.equalsIgnoreCase("n"))){
                                System.out.println("Error: Invalid Input: (y/n)>");
                            }
                        }
                        System.out.println();
                        if(answer.equalsIgnoreCase("y")){
                            continue;
                        } else if(answer.equalsIgnoreCase("n")){
                            keepSearching = false;
                            found = true;
                            break;
                        }
                    }
                }
            }
        }
        if(!found){
            System.out.println("==>> STRING " + pattern + " NOT FOUND <<==");
            main.lines.seek(originalIndex);
            return;
        }
    }

    // s - substitute all occurrences of text1 with text2 on current line
    public static void substituteTextDriver(String text1, String text2){
        String line = main.lines.getData();
        StringTokenizer tok = new StringTokenizer(line, " ");
        while(tok.hasMoreTokens()){
            String word = tok.nextToken();
            if(word.equals(text1)){
                int wordIndex = line.indexOf(word);
                line = line.substring(0, wordIndex) + text2 + line.substring(wordIndex + word.length());
                main.dirty = true;
            }
        }
        main.lines.setData(line);
    }

    // sr - substitute all occurrences of text1 with text2 between start and stop
    public static void substituteTextInRangeDriver(String text1, String text2, int start, int stop){
        int originalIndex = main.lines.getIndex();
        int size = main.lines.getSize();
        if(0 > start || start > size || 0 > stop || stop > size){
            System.out.println("==>> RANGE ERROR - start/stop MUST BE [1.." + size + "] <<==");
        } else {
            for(int i = start; i <= stop; i++){
                goToLineDriver(i);
                substituteTextDriver(text1, text2);
            }
        }
        main.lines.seek(originalIndex);
    }

    // d - delete the current line from buffer and copy into the clipboard
    public static void cutLineDriver(){
        if(!(clipboard.lines.isEmpty()) && count == 0){
            clipboard.lines.clear();
        }
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else {
            clipboard.lines.insertLast(main.lines.getData());
            main.lines.deleteAt();
            main.dirty = true;
        }
    }

    // dr - delete several lines from buffer and copy into the clipboard
    public static void cutLinesInRangeDriver(int start, int stop){
        int originalIndex = main.lines.getIndex();
        int size = main.lines.getSize();
        if(!(clipboard.lines.isEmpty())){
            clipboard.lines.clear();
        }
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else if(start < 0 || start > size || stop < 0 || stop > size){
            System.out.println("==>> INDICES OUT OF RANGE <<==");
        } else {
            count = 0;
            for(int i = stop; i >= start; i--){
                goToLineDriver(i);
                cutLineDriver();
                count++;
            }
        }
        main.lines.seek(originalIndex);
    }

    // c - copy current line into clipboard
    public static void copyDriver(){
        if(!(clipboard.lines.isEmpty()) && count == 0){
            clipboard.lines.clear();
        }
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else {
            clipboard.lines.insertFirst(main.lines.getData());
        }
    }

    // cr - copy lines between start and stop into the clipboard
    public static void copyLinesInRangeDriver(int start, int stop){
        int originalIndex = main.lines.getIndex();
        int size = main.lines.getSize();
        if(!(clipboard.lines.isEmpty())){
            clipboard.lines.clear();
        }
        if(main.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else if(start < 0 || start > size || stop < 0 || stop > size){
            System.out.println("==>> INDICES OUT OF RANGE <<==");
        } else {
            count = 0;
            for(int i = start; i <= stop; i++){
                goToLineDriver(i);
                copyDriver();
                count++;
            }
        }
        main.lines.seek(originalIndex);
    }

    // pa - paste the contents of the clipboard above the current line
    public static void pasteAboveDriver(){
        if(clipboard.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else {
            clipboard.lines.first();
            for(int i = 0; i < clipboard.lines.size; i++){
                main.lines.insertAt(clipboard.lines.getData());
                clipboard.lines.next();
                main.dirty = true;
            }
        }
    }

    // pb - paste the contents of the clipboard below the current line
    public static void pasteBelowDriver(){
        int index = main.lines.getIndex();
        if(clipboard.lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
        } else {
            clipboard.lines.last();
            for(int i = 0; i < clipboard.lines.size; i++){
                main.lines.next();
                if(main.lines.atLast()){
                    main.lines.insertLast(clipboard.lines.getData());
                } else {
                    main.lines.insertAt(clipboard.lines.getData());
                }
                clipboard.lines.previous();
                main.dirty = true;
            }
        }
        main.lines.seek(index);
    }

    // ia - insert new lines of text above the current line until "." appears on its own line
    public static void insertAboveDriver(){
        Scanner input = new Scanner(System.in);
        boolean stop = false;
        boolean empty = main.lines.isEmpty();
        while(!stop){
            main.dirty = true;
            System.out.print("> ");
            String line = input.nextLine();
            if(line.equals(".")){
                stop = true;
            } else {
                if(empty){
                    main.lines.insertLast(line);
                } else {
                    main.lines.insertAt(line);
                    main.lines.next();
                }
            }
        }
    }

    // ic - insert new lines of text at the current line until "." appears on its own line
    public static void insertCurrentDriver(){
        int originalIndex = main.lines.getIndex();
        int changingIndex = main.lines.getIndex();
        Scanner input = new Scanner(System.in);
        String lineToInsert = "";
        boolean stop = false;
        while(!stop){
            System.out.print("> ");
            lineToInsert = input.nextLine();
            if(!(lineToInsert.equals("."))){
                main.dirty = true;
                changingIndex++;
                main.lines.seek(changingIndex);
                if(main.lines.atLast()){
                    main.lines.insertLast(lineToInsert);
                } else {
                    main.lines.insertAt(lineToInsert);
                }
            } else {
                stop = true;
            }
        }
        main.lines.seek(originalIndex);
    }

    // ib - insert new lines of text after the current line until "." appears on its own line
    public static void insertBelowDriver(){
        int changingIndex = main.lines.getIndex();
        Scanner input = new Scanner(System.in);
        String lineToInsert = "";
        boolean stop = false;
        while(!stop){
            System.out.print("> ");
            lineToInsert = input.nextLine();
            if(!(lineToInsert.equals("."))){
                main.dirty = true;
                changingIndex++;
                main.lines.seek(changingIndex);
                if(main.lines.atLast()){
                    main.lines.insertLast(lineToInsert);
                } else {
                    main.lines.insertAt(lineToInsert);
                }
            } else {
                stop = true;
            }
        }
    }
}