package com.zsw.common.enums;

import com.zsw.common.enums.impl.IEnum;

public enum VoteType implements IEnum {
    
    //单选
    radio(1),
    //多选
    multiple(2);
    
    
    private int typeId;
    
    private VoteType(int typeId) {
        this.typeId = typeId;
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
