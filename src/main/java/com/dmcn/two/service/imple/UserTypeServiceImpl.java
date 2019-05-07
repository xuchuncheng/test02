package com.dmcn.two.service.imple;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.dmcn.two.mapper.UserTypeMapper;
import com.dmcn.two.entity.UserType;
import com.dmcn.two.service.UserTypeService;
/**
 * @Author: 徐本六
 * @Date: 2019/5/6 12:46
 * @Version 1.0
 * @maxim 蚍蜉撼大树 可敬不自量
 */
@Service
public class UserTypeServiceImpl implements UserTypeService{

    @Resource
    private UserTypeMapper userTypeMapper;

    @Override
    public int deleteByPrimaryKey(Integer typeId) {
        return userTypeMapper.deleteByPrimaryKey(typeId);
    }

    @Override
    public int insert(UserType record) {
        return userTypeMapper.insert(record);
    }

    @Override
    public int insertSelective(UserType record) {
        return userTypeMapper.insertSelective(record);
    }

    @Override
    public UserType selectByPrimaryKey(Integer typeId) {
        return userTypeMapper.selectByPrimaryKey(typeId);
    }

    @Override
    public int updateByPrimaryKeySelective(UserType record) {
        return userTypeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserType record) {
        return userTypeMapper.updateByPrimaryKey(record);
    }

}
