package com.dmcn.two.entity;

import java.io.Serializable;

/**
 * @Author: 徐本六
 * @Date: 2019/5/6 12:46
 * @Version 1.0
 * @maxim 蚍蜉撼大树 可敬不自量
 */
public class UserType implements Serializable {
    /**
    * 
    */
    private Integer typeId;

    /**
    * 
    */
    private String name;

    private static final long serialVersionUID = 1L;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}