package com.example.genericRowMapper;

import org.springframework.beans.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class NestedRowMapper<T> implements RowMapper<T> {

    private Class<T> mappedClass;

    public NestedRowMapper(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {

        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);

        List<String> fieldNames = GetObjectProperties.getFieldNames(mappedClass);

        bw.setAutoGrowNestedPaths(true);

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int index = 1; index <= columnCount; index++) {

            try {

                String column = JdbcUtils.lookupColumnName(metaData, index);
                Object value = JdbcUtils.getResultSetValue(rs, index, Class.forName(metaData.getColumnClassName(index)));

                bw.setPropertyValue(column, value);

            } catch (TypeMismatchException | NotWritablePropertyException | ClassNotFoundException e) {
                // Ignore
            }
        }

        return mappedObject;
    }
}
