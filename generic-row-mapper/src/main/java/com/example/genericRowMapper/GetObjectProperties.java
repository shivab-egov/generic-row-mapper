package com.example.genericRowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class GetObjectProperties {

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
