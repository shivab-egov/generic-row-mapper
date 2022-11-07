package com.example.genericRowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GetObjectProperties {
    static List<String> getFieldNames(Class className){
        List<String> fieldNames = new ArrayList<>();
        Field[] fields = className.getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            if(isWrapper(field) || field.getType().isPrimitive()){
                String fieldName = field.getName();
                fieldNames.add(fieldName);
            }else{
                Class clName = field.getType();
                fieldNames.addAll(getFieldNames(clName));
            }
        }
        return fieldNames;
    }

    static boolean isWrapper(Field field){
        Type type = field.getType();
        return (type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class);
    }
}
