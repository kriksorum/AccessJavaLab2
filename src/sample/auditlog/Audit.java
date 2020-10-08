package sample.auditlog;

import java.io.FileWriter;
import java.io.IOException;

public class Audit {
    static public void writeFile(String str){
        try (FileWriter file = new FileWriter("auditlog.txt", true)){
            file.write(str + "\n");
            file.close();
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }


    }
}
