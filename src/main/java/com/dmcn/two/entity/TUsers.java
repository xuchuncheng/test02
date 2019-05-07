package com.dmcn.two.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 徐本六
 * @Date: 2019/5/6 12:46
 * @Version 1.0
 * @maxim 蚍蜉撼大树 可敬不自量
 */
public class TUsers implements Serializable {
    /**
    * 使用序列赋值
    */
    private Integer id;

    /**
    * 长度6位（含）以上
    */
    private String name;

    /**
    * 长度6位（含）以上
    */
    private String password;

    /**
    * 1正常 0 冻结
    */
    private Integer status;

    /**
    * 
    */
    private Date lastModifyTime;

    /**
    * 对应用户类型表主键
    */
    private Integer typeId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}