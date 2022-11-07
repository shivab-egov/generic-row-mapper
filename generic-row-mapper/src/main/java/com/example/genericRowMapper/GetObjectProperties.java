package com.example.genericRowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class GetObjectProperties {
    static Map<String, Field> getFieldNames(Class className){
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = className.getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            fieldMap.put(field.getName(), field);
           /* if(isWrapper(field) || field.getType().isPrimitive()){
                String fieldName = field.getName();
                fieldMap.put(fieldName, field.getType());
            }else{
                Class clName = field.getType();
                fieldNames.addAll(getFieldNames(clName));
            }
            */
        }
        return fieldMap;
    }

    static boolean checkIfPropertyFoundInClass(Class className, String propertyName){
        try{
            Field f = className.getDeclaredField(propertyName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static boolean set(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    public static <V> V get(Object object, String fieldName){
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (V) field.get(object);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }

    static String checkIfFoundInNested(Map<String, Field> fieldMap, String property){
        Iterator it = fieldMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Field field = (Field) pair.getValue();
            if(isWrapper(field.getType()) || field.getType().isPrimitive()){
                continue;
            }
            if(checkIfPropertyFoundInClass(field.getType(), property)){
                return pair.getKey().toString();
            }
        }
        return null;
    }

    static boolean isWrapper(Field field){
        Type type = field.getType();
        return isWrapper(type);
    }

    static boolean isWrapper(Type type){
        return (type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class);
    }
}
