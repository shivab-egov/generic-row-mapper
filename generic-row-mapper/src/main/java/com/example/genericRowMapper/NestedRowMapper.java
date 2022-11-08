package com.example.genericRowMapper;

import org.springframework.beans.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NestedRowMapper<T> implements RowMapper<T> {

    private Class<T> mappedClass;

    public NestedRowMapper(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
    }
    private Object instantiate(Class<?> clazz, HashMap row) throws IllegalAccessException {
        Object mappedObject = BeanUtils.instantiateClass(clazz);
        Field[] fields = mappedObject.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (!f.getType().isPrimitive() && !GetObjectProperties.isWrapper(f)) {
                Object mappedNestedObject = instantiate(f.getType(), row);
                f.set(mappedObject, mappedNestedObject);
            }else{
                Object value = row.get(f.getName());
                f.set(mappedObject, value);
            }
        }
        return mappedObject;
    }

//    private Object setValue(Object object, HashMap row) throws IllegalAccessException {
//        Field[] fields = object.getClass().getDeclaredFields();
//        for (Field f : fields) {
//            f.setAccessible(true);
//            Object value = row.get(f.getName());
//            if(value == null)
//                value = setValue(f.get(object), row);
//
//            f.set(object, value);
//        }
//        return object;
//    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData meta_data = rs.getMetaData();
        int columnCount = meta_data.getColumnCount();

        HashMap row = new HashMap(columnCount);

        //Fill hashmap with columnName as Key and ColumnValue as value
        for (int index = 1; index <= columnCount; index++) {
            try {
                String column = JdbcUtils.lookupColumnName(meta_data, index);
                Object value = JdbcUtils.getResultSetValue(rs, index, Class.forName(meta_data.getColumnClassName(index)));
                row.put(column, value);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        T instance;

        try {
            instance = (T) instantiate(this.mappedClass, row);
            //instance = (T) setValue(instance, row);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return instance;
    }
}
