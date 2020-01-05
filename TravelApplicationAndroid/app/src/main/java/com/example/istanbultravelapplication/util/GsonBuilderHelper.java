package com.example.istanbultravelapplication.util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonBuilderHelper {

    public static Gson getJsonBuilder() {
        return new GsonBuilder().create();
    }

}
