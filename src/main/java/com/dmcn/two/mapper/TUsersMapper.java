package com.dmcn.two.mapper;
import org.apache.ibatis.annotations.Param;

import com.dmcn.two.entity.TUsers;

import java.util.List;

/**
 * @Author: 徐本六
 * @Date: 2019/5/6 12:46
 * @Version 1.0
 * @maxim 蚍蜉撼大树 可敬不自量
 */
public interface TUsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUsers record);

    int insertSelective(TUsers record);

    TUsers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUsers record);

    int updateByPrimaryKey(TUsers record);

     List<TUsers> find();


}