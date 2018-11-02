package com.rectangle.net.data.db;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rectangle.net.data.models.RectangleModel;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


public class DataTypeConverter implements Serializable {

    private static Gson gson = new Gson();
    @TypeConverter
    public static List<RectangleModel> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<RectangleModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ListToString(List<RectangleModel> someObjects) {
        return gson.toJson(someObjects);
    }

    @TypeConverter
    public String fromIntList(List<Integer> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Type type = new TypeToken<List<Integer>>() {}.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter
    public List<Integer> toIntList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }

        Type type = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }
}
