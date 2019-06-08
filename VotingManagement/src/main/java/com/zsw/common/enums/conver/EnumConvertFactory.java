package com.zsw.common.enums.conver;

import com.zsw.common.enums.impl.IEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class EnumConvertFactory implements ConverterFactory<String, IEnum> {

    /**
     * 
     * @param aClass
     * @param <T>   T是IEnum的一个子类
     * @return
     */
    @Override
    public <T extends IEnum> Converter<String, T> getConverter(Class<T> aClass) {
        return new StringToIEnumConver(aClass);
    }
    
    private final class StringToIEnumConver<T extends IEnum> implements Converter<String, T>{

        private Class<T> enumType;

        public StringToIEnumConver(Class<T> enumType) {
            this.enumType = enumType;
        }
        
        @Override
        public T convert(String sourceParam) {
            if (StringUtils.isEmpty(sourceParam)) {
                return null;
            }
            for (T t : enumType.getEnumConstants()) {
                if (t.getName().equalsIgnoreCase(sourceParam) ) {
                    return t;
                }
            }
            return  null;
        }
        
    }
}
