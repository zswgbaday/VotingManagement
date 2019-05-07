package com.zsw.common.util;

import java.util.UUID;

public class Uuid {

    /**
     * 得到32位的uuid
     * @return
     */
    public static String getUUID32(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


    public static String[] getUUID(int num){

        if ( num <= 0) {
            return null;
        }

        String[] uuidArr = new String[num];

        for (int i = 0; i < uuidArr.length; i++) {
            uuidArr[i] = getUUID32();
        }

        return uuidArr;
    }


}
