package com.zsw.common.exception;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomException extends RuntimeException {

    Logger log = LoggerFactory.getLogger(ServiceException.class);

    public CustomException(String strMsg, Object ...obj ) {
        super(doLog(strMsg, obj));
    }
    
    private static String doLog (String strMsg, Object ...obj) {
        String sign = "{}";
        StringBuilder msg = new StringBuilder();
        if (obj != null && obj.length > 0 && strMsg.indexOf(sign) != -1) {
            int index = 0;
            while (strMsg.indexOf(sign) != -1) {
                int signIndex = strMsg.indexOf(sign);
                String pre = strMsg.substring(0, signIndex);
                String suffix = strMsg.substring(signIndex + sign.length());
                msg.append(pre);
                msg.append("[" + obj[index ++] + "]");
                strMsg = suffix;
            }
            msg.append(strMsg);
        }else {
            msg.append(strMsg);
        }
        return msg.toString();
    }
    
}
