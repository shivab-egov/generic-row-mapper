package com.example.genericRowMapper;

import org.springframework.beans.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

public class NestedRowMapper<T> implements RowMapper<T> {

    private Class<T> mappedClass;

    public NestedRowMapper(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
    }
    private Object instantiate(Class<?> clazz, HashMap row) throws IllegalAccessException {
        Object mappedObject = BeanUtils.instantiateClass(clazz);
        Field[] fields = mappedObject.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
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

    private HashMap getMap(ResultSet rs) throws SQLException, ClassNotFoundException {
        ResultSetMetaData meta_data = rs.getMetaData();
        int columnCount = meta_data.getColumnCount();

        HashMap row = new HashMap(columnCount);

        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(meta_data, index);
            Object value = JdbcUtils.getResultSetValue(rs, index, Class.forName(meta_data.getColumnClassName(index)));
            row.put(column, value);
        }
        return row;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException{
        T instance;
        try {
            HashMap row = getMap(rs);
            instance = (T) instantiate(this.mappedClass, row);
        } catch (IllegalAccessException | SQLException | ClassNotFoundException e) {
            throw new SQLException(e.getMessage());
        }

        return instance;
    }
}
