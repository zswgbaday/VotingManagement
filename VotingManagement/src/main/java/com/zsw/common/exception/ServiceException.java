package com.zsw.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceException extends  CustomException {
    
    public ServiceException(String msg, Object ...obj) {
        super(msg, obj);
    }
}
