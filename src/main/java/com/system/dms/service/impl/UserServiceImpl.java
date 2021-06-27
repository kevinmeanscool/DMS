package com.system.dms.service.impl;

import com.system.dms.dao.UserMapper;
import com.system.dms.entity.ProjectMember;
import com.system.dms.entity.User;
import com.system.dms.service.UserService;
import com.system.dms.staticEnum.UserStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(User user) {
        if(userMapper.isUserNameExisting(user)==0){
            return this.userMapper.register(user);
        }
        else {
            return false;
        }
    }

    @Override
    public String login(User user) {
        if(userMapper.isUserNameExisting(user)==1){
            if(userMapper.isPasswordRight(user)==1){
                if(userMapper.online(user.getUsername(),UserStatusEnum.ONLINE.ordinal()))
                    return "success";
                else
                    return "fail to online";
            }
            else
                return "password is not right";
        }
        else
            return "username is not existing";
    }

    @Override
    public User getUserInfoById(long userId) {
        return userMapper.getUserInfoById(userId);
    }

    @Override
    public User getUserInfoByUsername(String username) {
        return userMapper.getUserInfoByUsername(username);
    }

    @Override
    public String logout(String username) {
        if(userMapper.offline(username,UserStatusEnum.OFFLINE.ordinal()))
            return "success";
        else
            return "fail to offline";
    }

    @Override
    public List<User> getAllUserInfo() {
        return userMapper.getAllUserInfo();
    }

    @Override
    public boolean uploadHeadImage(User user) {
        return userMapper.uploadHeadImage(user);
    }

    @Override
    public boolean updateUserInfoById(User user) {
        return userMapper.updateUserInfoById(user);
    }

    @Override
    public boolean changePasswordById(User user,String newPassword) {
        if(userMapper.isPasswordRight(user)==1)
            return userMapper.changePasswordById(user.getId(),newPassword);
        else
            return false;
    }

    @Override
    public boolean changePasswordByUsername(String username, String newPassword) {
        return userMapper.changePasswordByUsername(username,newPassword);
    }

    @Override
    public boolean isUsernameExisting(String username) {
        if(userMapper.isUserNameExisted(username)==1){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public List<String> getAllUsernameByProjectMemberList(List<ProjectMember> projectMemberList) {
        List<String> usernameList = new LinkedList<String>();
        int length = projectMemberList.size();
        int i = 0;
        while(i!=length){
            usernameList.add(userMapper.getUsernameById(projectMemberList.get(i).getUserid()));
            i++;
        }

        return usernameList;
    }

    @Override
    public List<String> getUsernamesByEmail(String email) {
        return userMapper.getUsernamesByEmail(email);
    }

    @Override
    public boolean isAccountCountRight(User user) {
        if(userMapper.isAccountCountRight(user)<6){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean changeIdentity(long userId, long identity) {
        return userMapper.changeIdentity(userId,identity);
    }
}
