package util;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WriteReadOperation {

    public static <T> ArrayList<T> getDataAsArrayList(String json, Class<T> className) {

        Type type = TypeToken.getParameterized(List.class, className).getType();
        ArrayList<T> data = GsonBuilderHelper.getJsonBuilder().fromJson(json, type);

        if (data != null)
            return data;

        return new ArrayList<>();
    }

    public static String convertToJSON(Object object) {
        return GsonBuilderHelper.getJsonBuilder().toJson(object);
    }

    public static <T> T convertToObject(String rawJson, Class<T> className) {
        return GsonBuilderHelper.getJsonBuilder().fromJson(rawJson,className);
    }


    public static String getContentFromFile(String filename){
        File file = new File(filename);
        if(!file.exists()){
            try {
                file.createNewFile();
                writeContentForAFile(filename,convertToJSON(new ArrayList<>()));
                return "";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            return scanner.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeContentForAFile(String filename,String jsonContent) {
        File file = new File(filename);
        PrintWriter out = null;
        try {
            out = new PrintWriter(file);
            out.print(jsonContent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.close();
    }
}
