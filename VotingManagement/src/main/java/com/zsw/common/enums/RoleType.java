package com.zsw.common.enums;

import com.zsw.common.enums.impl.IEnum;

public enum RoleType implements IEnum {
    
    //数字表示对应的role_id
    
    //超级管理员
    SUPERADMIN(0),
    //管理员
    ADMIN(1),
    //vip 待定
    VIP(2),
    //普通用户
    ORDINARY(3),
    //游客
    PASSER(4);
    
    private Integer value;
    
    private RoleType(Integer s) {
        this.value = s;
    }
    
    public Integer getValue(){
        return this.value;
    }
    
    public static RoleType getTypeByValue(Integer i) {
        switch (i){
            case 0 : return SUPERADMIN;
            case 1 : return ADMIN; 
            case 2 : return ORDINARY;
            case 3 : return PASSER; 
            default: return ORDINARY;
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
