package com.zsw.common.enums;

import com.zsw.common.enums.impl.IEnum;
import com.zsw.common.exception.ServiceException;

public enum VoteStatus implements IEnum {
    ACTIVE{
       
    },
    DISABLE{

    };
    
    public static VoteStatus getValue(String value) {
        try{
            return VoteStatus.valueOf(value);
        }catch ( IllegalArgumentException e) {
            throw   new ServiceException("投票状态不合法,请验证准确");
        }
    }

    @Override
    public IEnum getEnumValue() {
        return this;
    }

    @Override
    public String getName() {
        return this.name();
    }
    
    
}
