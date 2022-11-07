package com.example.genericRowMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class GenericRowMapper<T> implements RowMapper<T> {

    private final Class<T> mappedClass;

    public GenericRowMapper(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        // see if able to instantiate the class
        T mappedObject = BeanUtils.instantiateClass(mappedClass);
        log.info(mappedObject.toString());
        // see if able to distinguish the nested fields
        Field[] fields = mappedObject.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (!f.getType().isPrimitive() && !isWrapper(f)) {
                log.info(f.getName());
                // instantiate
                Object mappedNestedObject = BeanUtils.instantiateClass(f.getType());
                log.info(mappedNestedObject.toString());
                // check if instantiation is by reference
                log.info(mappedObject.toString());
                // set the newly instantiated object on the parent
                try {
                    f.set(mappedObject, mappedNestedObject);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                // check if nested object has been set on the parent object
                log.info(mappedObject.toString());
            }
        }
        return null;
    }

    static boolean isWrapper(Field field){
        Type type = field.getType();
        return (type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class);
    }
}
