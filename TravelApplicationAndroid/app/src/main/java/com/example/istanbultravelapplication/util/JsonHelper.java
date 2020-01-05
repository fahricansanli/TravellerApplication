package com.example.istanbultravelapplication.util;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {
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
}
