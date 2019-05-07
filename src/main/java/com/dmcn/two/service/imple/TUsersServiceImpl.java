package com.dmcn.two.service.imple;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.dmcn.two.entity.TUsers;
import com.dmcn.two.mapper.TUsersMapper;
import com.dmcn.two.service.TUsersService;

import java.util.List;

/**
 * @Author: 徐本六
 * @Date: 2019/5/6 12:46
 * @Version 1.0
 * @maxim 蚍蜉撼大树 可敬不自量
 */
@Service
public class TUsersServiceImpl implements TUsersService{

    @Resource
    private TUsersMapper tUsersMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return tUsersMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(TUsers record) {
        return tUsersMapper.insert(record);
    }

    @Override
    public int insertSelective(TUsers record) {
        return tUsersMapper.insertSelective(record);
    }

    @Override
    public TUsers selectByPrimaryKey(Integer id) {
        return tUsersMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(TUsers record) {
        return tUsersMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TUsers record) {
        return tUsersMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<TUsers> findAll() {
        return tUsersMapper.find();
    }
}
