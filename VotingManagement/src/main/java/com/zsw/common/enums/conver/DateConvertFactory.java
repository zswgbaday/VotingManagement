package com.zsw.common.enums.conver;

import com.zsw.common.enums.impl.IEnum;
import com.zsw.common.exception.CustomException;
import com.zsw.common.util.JSONUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvertFactory implements ConverterFactory<String, Date> {


    @Override
    public <T extends Date> Converter<String, T> getConverter(Class<T> aClass) {
        return new DateConvertFactory.StringToDateConver();
    }

    public class StringToDateConver<T extends Date> implements Converter<String, T> {

        private String pattern = "yyyy-MM-dd HH:mm:ss";
        @Override
        public T convert(String s) {
            Date date = null;
            try {
                DateFormat ds = new SimpleDateFormat(pattern);
                date = ds.parse(s);
            } catch (ParseException e) {
                throw new CustomException("日期格式应该如：{}" ,pattern);
            }
            return (T) date;
        }
    }
}
