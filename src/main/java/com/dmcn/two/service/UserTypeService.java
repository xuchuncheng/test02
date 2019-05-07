package com.dmcn.two.service;

import com.dmcn.two.mapper.UserTypeMapper;
import com.dmcn.two.entity.UserType;
    /**
 * @Author: 徐本六
 * @Date: 2019/5/6 12:46
 * @Version 1.0
 * @maxim 蚍蜉撼大树 可敬不自量
 */
public interface UserTypeService{


    int deleteByPrimaryKey(Integer typeId);

    int insert(UserType record);

    int insertSelective(UserType record);

    UserType selectByPrimaryKey(Integer typeId);

    int updateByPrimaryKeySelective(UserType record);

    int updateByPrimaryKey(UserType record);

}
