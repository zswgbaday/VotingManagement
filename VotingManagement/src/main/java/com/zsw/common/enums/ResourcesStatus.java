package com.zsw.common.enums;

import com.zsw.common.enums.impl.IEnum;

public enum ResourcesStatus implements IEnum {
    ACTIVE{
        
    },
    DISABLE{
    };

    @Override
    public ResourcesStatus getEnumValue() {
        return this;
    }

    @Override
    public String getName() {
        return this.name();
    }
    
}
