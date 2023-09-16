// imports
import java.io.*;
import java.util.*;

public class Buffer {
    // data members
    public static Scanner scanner;
    String filename;
    boolean dirty;
    DLList<String> lines = new DLList<String>();

    // member functions

    // default constructor
    public Buffer(){

    }

    // constructor
    public Buffer(String name){
        this.filename = name;
        dirty = false;
        read();
    }

    public void read(){
        File file = new File(filename);

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e){
            System.out.println("==>> FILE DOES NOT EXIST <<==");
        }

        if(scanner != null){
            while(scanner.hasNextLine()){
                lines.insertLast(scanner.nextLine());
            }
            // starts you at the top of the list when you open the file
            if(!(lines.isEmpty())){
                lines.first();
            }
        }
    }

    public void write(){
        if(lines.isEmpty()){
            System.out.println("==>> BUFFER IS EMPTY <<==");
            return;
        } else {
            lines.first();
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
                while (lines.hasNext() || lines.atLast()){
                    bw.write(lines.getData());
                    bw.newLine();
                    if(lines.atLast()){
                        break;
                    }
                    lines.next();
                }
                bw.close();
            } catch (IOException e){
                System.out.println("error writing to file  " + filename);
            }
            dirty = false;
        }
    }
}