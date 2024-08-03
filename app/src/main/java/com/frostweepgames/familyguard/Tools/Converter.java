package com.frostweepgames.familyguard.Tools;

import android.content.ContentValues;

import com.frostweepgames.androidhelperlibrary.tools.SerializationTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Converter {

    public static HashMap<String, Object> getMapWithListFrom(String key, Object value) {
        ArrayList<Object> list = new ArrayList<>();
        list.add(value);

        HashMap<String, Object> map = new HashMap<>();

        map.put(key, list);

        return map;
    }


    public static ContentValues prepareContentValuesFromList(HashMap<String, Object> content) {
        ContentValues contentValues = new ContentValues();

        String value;
        Object val;
        for (Map.Entry<String, Object> entry : content.entrySet()) {
            val = entry.getValue();

            if (val.getClass() != String.class)
                value = SerializationTool.serializeObject(val);
            else
                value = (String) val;

            contentValues.put(entry.getKey(), value);
        }
        return contentValues;
    }
}