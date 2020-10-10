package sample.auditlog;

import java.io.*;
import java.util.ArrayList;

public class Audit {
    static public void writeFile(String str){

        try {
            FileWriter file = new FileWriter("auditlog.txt", true);
            file.write(str + "\n");
            file.close();
        } catch (IOException ex){
            System.out.println(ex.getMessage());

        }


    }

    public static ArrayList readFile(){
        ArrayList<String> strList = new ArrayList();
        try {
            File file = new File("auditlog.txt");
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                strList.add(line);
                // считываем остальные строки в цикле
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strList;
    }
}
