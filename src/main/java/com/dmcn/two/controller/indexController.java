package com.dmcn.two.controller;

import com.dmcn.two.common.utils.JsonUtil;
import com.dmcn.two.entity.TUsers;
import com.dmcn.two.service.TUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: 徐本六
 * @Date: 2019/5/6 12:47
 * @Version 1.0
 * @maxim 蚍蜉撼大树 可敬不自量
 */

@RestController
public class indexController {
    @Resource
    private TUsersService tUsersService;

    private String json = JsonUtil.getResponseJson(2000, "err", null);

    @PostMapping("initTab")
    public String initTab() {
        List<TUsers> tUsers = tUsersService.findAll();
        if (tUsers != null) {
            json = JsonUtil.getResponseJson(0, "ok", tUsers);
        }
        return json;
    }

    @PostMapping("register")
    public String addUser(TUsers users) {
        Date date =new Date();
        users.setLastModifyTime(date);
        int row = tUsersService.insertSelective(users);
        if (row>0){
            json = JsonUtil.getResponseJson(0, "ok", row);
        }
        return json;
    }

    @PostMapping("updUser")
    public String updUser(TUsers users) {
        int row = tUsersService.updateByPrimaryKeySelective(users);
        if (row>0){
            json = JsonUtil.getResponseJson(0, "ok", row);
        }
        return json;
    }

    @PostMapping("delUser")
    public String delUser(@RequestParam(value = "id") int id){
        int row = tUsersService.deleteByPrimaryKey(id);
        if (row>0){
            json = JsonUtil.getResponseJson(0, "ok", row);
        }
        return json;
    }
}
