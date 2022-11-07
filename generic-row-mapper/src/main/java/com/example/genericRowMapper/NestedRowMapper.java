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
import java.util.List;
import java.util.Map;

public class NestedRowMapper<T> implements RowMapper<T> {

    private Class<T> mappedClass;

    public NestedRowMapper(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
    }

    T getNewInstance(){
        Object instance;
        try {
            instance = this.mappedClass.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
        return (T) instance;
    }
/*
    key = column name, value = type
    FieldMap = {
        'id': int,
        'phonenumber': string,
        'amount': AMOUNT
    }

    checkIfPropertyFound(Class v, Stirng col){
        return if col found in v
    }

    checkIfFoundInNestedObject(FieldMap map, String col){
        for q, v in fieldMap.items(){
            if(v is primitive or wrapper)
                continue;

            if(checkIfPropertyFound(v, q))
                return q;
        }
    }

    queryColumns = ['id', 'phonenumber', 'price'];
    for q in queryColumns:
        if q in fieldMap.keys():
            //Map the value;
        else
           keyName = checkIfFoundInNestedObject(fieldMap, q);
           //get object through key
           //apply value;
 */
    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {

        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);

        Map<String, Field> fieldMap = GetObjectProperties.getFieldNames(mappedClass);

        bw.setAutoGrowNestedPaths(true);

        ResultSetMetaData meta_data = rs.getMetaData();
        int columnCount = meta_data.getColumnCount();

        T instance = getNewInstance();


        for (int index = 1; index <= columnCount; index++) {

            try {
                String column = JdbcUtils.lookupColumnName(meta_data, index);
                Object value = JdbcUtils.getResultSetValue(rs, index, Class.forName(meta_data.getColumnClassName(index)));
                if(fieldMap.getOrDefault(column, null) != null){
                    GetObjectProperties.set(instance, column, value);
                }else{
                    String propertyName = GetObjectProperties.checkIfFoundInNested(fieldMap, column);
                    Field field = fieldMap.getOrDefault(propertyName, null);
                    if(field == null){
                        continue;
                    }

                    Object nestedObject = GetObjectProperties.get(instance, propertyName);
                    if(nestedObject == null){
                        nestedObject = field.getType().getConstructor().newInstance();
                        GetObjectProperties.set(instance, propertyName, nestedObject);
                    }
                    GetObjectProperties.set(nestedObject, column, value);
                }

            } catch (Exception e) {
                // Ignore
                System.out.println(e.getMessage());
            }
        }

        return instance;
    }
}
