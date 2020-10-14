package sample.auditlog;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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

    public static ArrayList readFile(String name){
        ArrayList<String> strList = new ArrayList();
        try {
            File file = new File(name);
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

    public static void zipAudit(String name){
        try {
            String sourceFile = "auditlog.txt";
            FileOutputStream fos = new FileOutputStream(name + ".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(sourceFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);
            byte[]bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.close();
            fis.close();
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void cleanAudit(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("auditlog.txt");
            writer.print("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.close();
    }

    public static boolean unZip(String name){
        String fileZip = name + ".zip";
        File destDir = new File("E:/arsalan/esstu/ProtectInfo/testAccess/unzip");
        byte[]buffer = new byte[1024];
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(fileZip));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }




    public static File newFile(File destinationDir, ZipEntry zipEntry) {
        File destFile = null;
        try{
            destFile = new File(destinationDir, zipEntry.getName());

            String destDirPath = destinationDir.getCanonicalPath();
            String destFilePath = destFile.getCanonicalPath();

            if (!destFilePath.startsWith(destDirPath + File.separator)) {
                throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return destFile;
    }

    public static void deleteFile(){
        File file = new File("unzip/auditlog.txt");

        if (file.delete()) {
            System.out.println(file.getName() + " deleted");
        } else {
            System.out.println(file.getName() + " not deleted");
        }
    }
}
