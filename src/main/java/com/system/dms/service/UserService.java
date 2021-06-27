package com.system.dms.service;

import com.system.dms.entity.ProjectMember;
import com.system.dms.entity.User;

import java.util.List;

public interface UserService {
    boolean register(User user);
    String login(User user);
    User getUserInfoById(long userId);
    User getUserInfoByUsername(String username);
    String logout(String username);
    List<User> getAllUserInfo();
    boolean uploadHeadImage(User user);
    boolean updateUserInfoById(User user);
    boolean changePasswordById(User user,String newPassword);
    boolean changePasswordByUsername(String username,String newPassword);
    boolean isUsernameExisting(String username);
    List<String> getAllUsernameByProjectMemberList(List<ProjectMember> projectMemberList);
    List<String> getUsernamesByEmail(String email);
    boolean isAccountCountRight(User user);
    boolean changeIdentity(long userId,long identity);
}
